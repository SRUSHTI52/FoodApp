package com.example.foodapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.example.foodapp.databinding.LoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: LoginBinding
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signInButton: Button
    private lateinit var noacc: TextView

    companion object {
        private const val TAG = "Login"
        private const val REQ_ONE_TAP = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        oneTapClient = Identity.getSignInClient(this)

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        noacc = findViewById(R.id.noaccount)
        noacc.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        signInButton = findViewById(R.id.button3)
        signInButton.setOnClickListener {
            startGoogleSignIn()
        }

        binding.button4.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                signInWithEmail(email, pass)
            } else {
                Toast.makeText(this@Login, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGoogleSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(result.pendingIntent.intentSender, REQ_ONE_TAP, null, 0, 0, 0, null)
                } catch (e: Exception) {
                    Log.e(TAG, "Google Sign-In failed", e)
                }
            }
            .addOnFailureListener(this) { e ->
                Log.e(TAG, "Google Sign-In failed", e)
            }
    }

    private fun signInWithEmail(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser
                checkUserInRoom(user?.email?:"")
            } else {
                Toast.makeText(this@Login, "Incorrect Login Details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUserInRoom(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = CartDatabase.getDatabase(application).userDao()
            val user = userDao.login(email)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    onLoginSuccess(user)
                    val intent = Intent(this@Login, Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@Login, "User not found in local database", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_ONE_TAP) {
            try {
                val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = googleCredential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "signInWithCredential:success")
                                val user = auth.currentUser
                                navigateToMainScreen(user)
                            } else {
                                Log.w(TAG, "signInWithCredential:failure", task.exception)
                                navigateToMainScreen(null)
                            }
                        }
                } else {
                    Log.d(TAG, "No ID token!")
                }
            } catch (e: ApiException) {
                Log.e(TAG, "Sign-in failed", e)
            }
        }
    }

    private fun navigateToMainScreen(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, Location::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.w(TAG, "Sign-in failed or user is null")
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        navigateToMainScreen(currentUser)
    }
    fun onLoginSuccess(user: User) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("user_id", user.id)
        editor.apply()

    }
}




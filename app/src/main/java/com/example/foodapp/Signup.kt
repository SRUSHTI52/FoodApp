package com.example.foodapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.databinding.SignupBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Signup : AppCompatActivity() {

    private lateinit var binding: SignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signInButton: Button
    private lateinit var yesacc: TextView

    companion object {
        private const val TAG = "Signup"
        private const val REQ_ONE_TAP = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        oneTapClient = Identity.getSignInClient(this)

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.client_id))  // Your server's client ID
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        yesacc = findViewById(R.id.yesaccount)
        yesacc.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        binding.button4.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                checkIfUserExists(email, password)
            } else {
                Toast.makeText(this@Signup, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        signInButton = findViewById(R.id.button3)
        signInButton.setOnClickListener {
            startGoogleSignIn()
        }
    }

    private fun checkIfUserExists(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = CartDatabase.getDatabase(application).userDao()
            val user = userDao.login(email)  // Adjust query as needed
            withContext(Dispatchers.Main) {
                if (user == null) {
                    // User does not exist in Room, create new user
                    createUserWithEmail(email, password)
                } else {
                    Toast.makeText(this@Signup, "User already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createUserWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    addUserToRoom(email, password, "USER")
                    navigateToMainScreen(user)
                } else {
                    when (task.exception?.localizedMessage) {
                        "The email address is already in use by another account." -> {
                            Toast.makeText(this@Signup, "Email already exists", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Log.d(TAG, "Error: ${task.exception?.localizedMessage}")
                        }
                    }
                }
            }
    }

    private fun addUserToRoom(email: String, password: String, role: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = CartDatabase.getDatabase(application).userDao()
            userDao.insertUser(User(username = email, password = password, role = role))
            val user = userDao.getUserByEmail(email)
            if (user != null) {
                onLoginSuccess(user)
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
            val intent = Intent(this,Home::class.java)
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
        editor.putInt("user_id", user.id) // Store user ID
        editor.apply()

    }
}




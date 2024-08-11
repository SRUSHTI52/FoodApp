package com.example.foodapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class WelcomePage : AppCompatActivity() {

    private  lateinit var admin: Button
  private  lateinit var customer: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)

        admin = findViewById(R.id.admin)
        admin.setOnClickListener {
            val intent = Intent(this, AdminLogin::class.java)
            startActivity(intent)
        }

        customer = findViewById(R.id.customer)
        customer.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }



    }








}
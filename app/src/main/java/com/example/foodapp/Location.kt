package com.example.foodapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.databinding.LocationBinding

class Location : AppCompatActivity() {

    lateinit var next: Button
    private val binding : LocationBinding by lazy{
        LocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.next.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
        val locationList = arrayOf("Airoli","Thane","Kalyan","Dombivili", )
        val adapter = ArrayAdapter(this,R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView = binding.locationlist
        autoCompleteTextView.setAdapter(adapter)
    }
}
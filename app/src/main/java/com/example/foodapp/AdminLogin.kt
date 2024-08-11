package com.example.foodapp
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import androidx.cardview.widget.CardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AdminLogin : AppCompatActivity() {

    private lateinit var card_order : CardView
    private lateinit var card_item : CardView
    private lateinit var amt : TextView
    private lateinit var orderHistoryDao: OrderHistoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminlogin)

        amt = findViewById(R.id.amt)

       card_order = findViewById(R.id.card_order)
        card_item = findViewById(R.id.card_item)

        orderHistoryDao = CartDatabase.getDatabase(this).orderHistoryDao()

        fetchTotalAmount()

        card_order.setOnClickListener {
            val intent = Intent(applicationContext, AdminOrders::class.java)
            startActivity(intent)
        }
        card_item.setOnClickListener {
            val intent = Intent(applicationContext, AdminItems::class.java)
            startActivity(intent)
        }

    }
    private fun fetchTotalAmount() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val totalAmount = orderHistoryDao.getTotalAmount()
                withContext(Dispatchers.Main) {
                    amt.text = "Total Amount: $totalAmount"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

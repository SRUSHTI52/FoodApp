package com.example.foodapp

import CartViewModel
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.ActivityPurchaseBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class PurchaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPurchaseBinding
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var orderHistoryDao: OrderHistoryDao
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderHistoryDao = CartDatabase.getDatabase(this).orderHistoryDao()

        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            userId = sharedPreferences.getInt("user_id", -1)
        }
   //     Toast.makeText(this, "user : ${userId}", Toast.LENGTH_SHORT).show()
        cartViewModel.setUserId(userId)
        cartViewModel.cartItems.observe(this) { cartItems ->
       //     Toast.makeText(this, "size : ${cartItems.size}", Toast.LENGTH_SHORT).show()
            val totalAmount = cartItems.sumOf { it.price * it.quantity }
            val orderItems = cartItems.map { OrderItem(it.id, it.name, it.imageResource, it.price, it.quantity) }

            binding.purchaseRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.purchaseRecyclerView.adapter = PurchaseAdapter(orderItems)

            binding.totalAmount.text = "Total Amount: $totalAmount"

            binding.proceedButton.setOnClickListener {
                if (cartItems.isEmpty()) {
                    Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val orderHistory = OrderHistory(
                            orderDate = getCurrentDate(),
                            orderTime = getCurrentTime(),
                            totalAmount = totalAmount,
                            items = orderItems,
                            userId = userId
                        )
                        orderHistoryDao.insertOrder(orderHistory)
                        cartViewModel.clearCart()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@PurchaseActivity, "Order confirmed!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            currentDate.format(formatter)
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.format(Date())
        }
    }

    private fun getCurrentTime(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentTime = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            currentTime.format(formatter)
        } else {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            sdf.format(Date())
        }
    }
}







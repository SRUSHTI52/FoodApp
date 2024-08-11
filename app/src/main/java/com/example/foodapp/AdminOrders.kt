package com.example.foodapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.ActivityAdminordersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminOrders : AppCompatActivity() {
    private lateinit var binding: ActivityAdminordersBinding
    private val orderHistoryViewModel: OrderHistoryViewModel by viewModels()
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminordersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordersAdapter = OrdersAdapter(emptyList())
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.ordersRecyclerView.adapter = ordersAdapter

        orderHistoryViewModel.orders.observe(this) { orders ->
            ordersAdapter.updateOrders(orders)
        }
    }
}


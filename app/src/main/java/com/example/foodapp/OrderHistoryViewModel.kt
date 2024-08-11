package com.example.foodapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class OrderHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val orderHistoryDao: OrderHistoryDao = CartDatabase.getDatabase(application).orderHistoryDao()
    val orders: LiveData<List<OrderHistory>> = orderHistoryDao.getAllOrdersAdmin()
}

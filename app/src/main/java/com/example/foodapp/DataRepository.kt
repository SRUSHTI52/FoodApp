package com.example.foodapp

import android.app.Application
import androidx.lifecycle.LiveData

class DataRepository(application: Application) {
    private val userDao: UserDao = CartDatabase.getDatabase(application).userDao()
    private val cartDao: CartItemDao = CartDatabase.getDatabase(application).cartItemDao()
    private val orderHistoryDao: OrderHistoryDao = CartDatabase.getDatabase(application).orderHistoryDao()

    suspend fun clearUserData(userId : Int) {
      //  userDao.deleteUser()
        cartDao.deleteAllCartItems(userId)
        orderHistoryDao.deleteAllOrders(userId)
    }
}

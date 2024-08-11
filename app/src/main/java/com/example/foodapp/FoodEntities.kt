package com.example.foodapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val imageUrl: Int
)

@Entity(tableName = "cart_items")
data class CartItems(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val itemId: Int,
    val quantity: Int
)

@Entity(tableName = "order_history")
data class OrderHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val orderDate: String,
    val orderTime: String,
    val totalAmount: Double,
    val items: List<OrderItem>
)

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUrl: Int,
    val price: Double,
    val quantity: Int
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String,
    val role: String // "USER" or "ADMIN"
)

//@Entity(tableName = "orders")
//data class Order(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val userId: Int,
//    val totalAmount: Double
//)
//
//@Entity(tableName = "order_items")
//data class OrderItem(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val orderId: Int,
//    val itemId: Int,
//    val quantity: Int
//)
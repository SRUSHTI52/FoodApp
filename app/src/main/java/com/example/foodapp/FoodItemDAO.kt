package com.example.foodapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getAllItems(): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("DELETE FROM items WHERE id = :itemId")
    suspend fun deleteItem(itemId: Int)

    @Query("SELECT * FROM items ORDER BY id LIMIT 4")
    suspend fun getFirstFourItems(): List<Item>

    @Delete
    suspend fun deleteItems(items: List<Item>)

    @Query("SELECT imageUrl FROM items WHERE id = :itemId")
    suspend fun getImageById(itemId: Int): Int

    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): Item

    @Query("SELECT * FROM items WHERE name LIKE :query")
    fun searchItems(query: String): List<Item>

}

//@Dao
//interface CartItemDao {
//    @Query("SELECT * FROM cart_items")
//    fun getCartItems(): LiveData<List<CartItem>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCartItem(cartItem: CartItem)
//
//    @Update
//    suspend fun updateCartItem(cartItem: CartItem)
//
//    @Delete
//    suspend fun deleteCartItem(cartItem: CartItem)
//
//    @Query("DELETE FROM cart_items")
//    suspend fun deleteAllCartItems()
//}
@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: Int): LiveData<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun deleteAllCartItems(userId: Int)

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    suspend fun getCartItemCount(userId: Int): Int


}


//@Dao
//interface OrderDao {
//    @Query("SELECT * FROM orders")
//    fun getAllOrders(): LiveData<List<Order>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOrder(order: Order)
//
//    @Query("SELECT SUM(totalAmount) FROM orders")
//    suspend fun getTotalIncome(): Double
//}
//@Dao
//interface OrderHistoryDao {
//    @Insert
//    suspend fun insertOrder(orderHistory: OrderHistory)
//
//    @Query("SELECT * FROM order_history ORDER BY id DESC")
//    fun getAllOrders(): LiveData<List<OrderHistory>>
//
//    @Query("DELETE FROM order_history")
//    suspend fun deleteAllOrders()
//
//}
@Dao
interface OrderHistoryDao {
    @Insert
    suspend fun insertOrder(orderHistory: OrderHistory)

    @Query("SELECT * FROM order_history WHERE userId = :userId ORDER BY id DESC")
    fun getAllOrders(userId: Int): LiveData<List<OrderHistory>>

    @Query("DELETE FROM order_history WHERE userId = :userId")
    suspend fun deleteAllOrders(userId: Int)

    @Query("SELECT SUM(totalAmount) FROM order_history")
    suspend fun getTotalAmount(): Double

    @Query("SELECT * FROM order_history")
    fun getAllOrdersAdmin(): LiveData<List<OrderHistory>>
}


@Dao
interface OrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItem)

    @Query("SELECT * FROM order_items WHERE id = :id")
    fun getOrderItems(id: Int): LiveData<List<OrderItem>>
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun login(username: String): User?

    @Query("SELECT * FROM users WHERE username = :email")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}



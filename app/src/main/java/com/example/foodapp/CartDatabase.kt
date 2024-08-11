package com.example.foodapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Item::class, CartItem::class, OrderHistory::class, OrderItem::class, User::class], version = 3)
@TypeConverters(Converters::class)
abstract class CartDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun cartItemDao(): CartItemDao
   // abstract fun orderDao(): OrderDao
   abstract fun orderHistoryDao(): OrderHistoryDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: CartDatabase? = null

        fun getDatabase(context: Context): CartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CartDatabase::class.java,
                    "food_app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

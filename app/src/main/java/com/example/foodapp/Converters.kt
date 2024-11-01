package com.example.foodapp

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromOrderItemList(value: List<OrderItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<OrderItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toOrderItemList(value: String): List<OrderItem> {
        val gson = Gson()
        val type = object : TypeToken<List<OrderItem>>() {}.type
        return gson.fromJson(value, type)
    }
}

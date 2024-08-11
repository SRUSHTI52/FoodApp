package com.example.foodapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemsViewModel(application: Application) : AndroidViewModel(application) {
    private val itemDao: ItemDao = CartDatabase.getDatabase(application).itemDao()
    val items: LiveData<List<Item>> = itemDao.getAllItems()
    fun updateItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.updateItem(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.deleteItem(item.id)
        }
    }
}


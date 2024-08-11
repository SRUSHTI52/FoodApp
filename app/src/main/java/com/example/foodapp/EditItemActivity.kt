package com.example.foodapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditItemActivity : AppCompatActivity() {

    private lateinit var itemDao: ItemDao
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        itemDao = CartDatabase.getDatabase(this).itemDao()

        val itemNameEditText = findViewById<EditText>(R.id.itemNameEditText)
        val itemPriceEditText = findViewById<EditText>(R.id.itemPriceEditText)
        val updateButton = findViewById<Button>(R.id.updateButton)

        itemId = intent.getIntExtra("ITEM_ID", -1)
        if (itemId != -1) {
            loadItemDetails(itemId)
        }

        updateButton.setOnClickListener {
            val updatedName = itemNameEditText.text.toString()
            val updatedPrice = itemPriceEditText.text.toString().toDoubleOrNull() ?: 0.0

            if (itemId != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    val image = getimage(itemId)
                    val updatedItem = Item(id = itemId, name = updatedName, price = updatedPrice, imageUrl = image)
                    updateItemInDatabase(updatedItem)
                }
            }
        }
    }

    private suspend fun getimage(itemId: Int): Int {
        return try {
            itemDao.getImageById(itemId)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    private fun loadItemDetails(itemId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val item = itemDao.getItemById(itemId)
                item?.let {
                    withContext(Dispatchers.Main) {
                        findViewById<EditText>(R.id.itemNameEditText).setText(it.name)
                        findViewById<EditText>(R.id.itemPriceEditText).setText(it.price.toString())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateItemInDatabase(item: Item) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                itemDao.updateItem(item)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditItemActivity, "Item updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

package com.example.foodapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminItems : AppCompatActivity() {

    private val itemsViewModel: ItemsViewModel by viewModels()
    private lateinit var itemsAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminitems)

        val recyclerView = findViewById<RecyclerView>(R.id.itemRecyclerView)
        itemsAdapter = ItemsAdapter(emptyList(), itemsViewModel)
        recyclerView.adapter = itemsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        itemsViewModel.items.observe(this, { items ->
            items?.let {
                itemsAdapter.updateItems(it)
            }
        })
    }
}



package com.example.foodapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.OrderHistoryItemBinding

class OrderHistoryAdapter(
    private val orders: List<OrderHistory>
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    inner class OrderViewHolder(private val binding: OrderHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderHistory) {
            binding.orderDate.text = order.orderDate
            binding.orderTime.text = order.orderTime
            binding.totalAmount.text = order.totalAmount.toString()

            // Display order items
            val itemsAdapter = OrderItemsAdapter(order.items)
            binding.itemsRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            binding.itemsRecyclerView.adapter = itemsAdapter
        }
    }
}

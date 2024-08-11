package com.example.foodapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.OrderItemBinding

class OrderItemsAdapter(
    private val items: List<OrderItem>
) : RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class OrderItemViewHolder(private val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.itemName.text = item.name
            binding.itemPrice.text = item.price.toString()
            binding.itemQuantity.text = item.quantity.toString()
            binding.itemImage.setImageResource(item.imageUrl)
        }
    }
}

package com.example.foodapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.ItemPurchaseBinding

class PurchaseAdapter(private val orderItems: List<OrderItem>) :
    RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val binding = ItemPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PurchaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        holder.bind(orderItems[position])
    }

    override fun getItemCount(): Int = orderItems.size

    inner class PurchaseViewHolder(private val binding: ItemPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderItem: OrderItem) {
            binding.itemImage.setImageResource(orderItem.imageUrl)
            binding.itemName.text = orderItem.name
            binding.itemPrice.text = "Price: ${orderItem.price}"
            binding.itemQuantity.text = "Quantity: ${orderItem.quantity}"
        }
    }
}

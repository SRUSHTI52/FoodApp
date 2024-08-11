package com.example.foodapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.AdminitemBinding


class ItemsAdapter(
    private var items: List<Item>,
    private val viewModel: ItemsViewModel
) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdminitemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val binding: AdminitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.itemName.text = item.name
            binding.itemPrice.text = item.price.toString()
            binding.itemImage.setImageResource(item.imageUrl)

            binding.itemdel.setOnClickListener {
                viewModel.deleteItem(item)
            }

            binding.itemedit.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, EditItemActivity::class.java).apply {
                    putExtra("ITEM_ID", item.id)
                }
                context.startActivity(intent)
            }
        }
    }
}

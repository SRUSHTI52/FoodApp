package com.example.foodapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.MenuItemBinding
import java.security.PrivateKey

class MenuAdapter(
    private val menuItems: MutableList<String>,
    private val menuItemPrice: MutableList<Double>,
    private val menuImage: MutableList<Int>,
    private val userId : Int
    ,private val addToCart: (String, Double, Int, Int) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                menuname.text = menuItems[position]
                menuprice.text = menuItemPrice[position].toString()
                menupic.setImageResource(menuImage[position])
                root.setOnClickListener { addToCart(menuItems[position], menuItemPrice[position], menuImage[position], userId) }

            }
        }
    }
}

package com.example.foodapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.PopularItemBinding

class HomeAdapter(
    private val foodNames: List<String>,
    private val prices: List<Double>,
    private val images: List<Int>,
    private val userId: Int?,
    private val addToCartCallback: (String, Double, Int, Int) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(foodNames[position], prices[position], images[position], userId)
    }

    override fun getItemCount(): Int = foodNames.size

    inner class HomeViewHolder(private val binding: PopularItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, price: Double, imageResource: Int, userId: Int?) {
            binding.name.text = name
            binding.price.text = price.toString()
            binding.pic.setImageResource(imageResource)
            binding.addtocart.setOnClickListener {
                if (userId != null) {
                    addToCartCallback(name, price, imageResource, userId)
                    Toast.makeText(binding.root.context, "Added to cart $userId", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(binding.root.context, "User ID is not set", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


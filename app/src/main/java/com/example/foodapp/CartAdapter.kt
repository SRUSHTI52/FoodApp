package com.example.foodapp

import CartViewModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.CartItemBinding

class CartAdapter(
    private val context: Context,
    private val cartViewModel: CartViewModel,
    private val userId: Int
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private var cartItems: List<CartItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       // Toast.makeText(context, "cart size : ${cartItems.size}", Toast.LENGTH_SHORT).show()
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
       // Toast.makeText(context, "cart size : ${newCartItems}", Toast.LENGTH_SHORT).show()

    }

    inner class CartViewHolder(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.apply {
                cartname.text = cartItem.name
                cartprice.text = cartItem.price.toString()
                food.setImageResource(cartItem.imageResource)
                cartcount.text = cartItem.quantity.toString()

                min.setOnClickListener {
                    if (cartItem.quantity > 1) {
                        cartItem.quantity--
                        cartViewModel.updateCartItem(cartItem.copy(userId = userId))
                    }
                }

                max.setOnClickListener {
                    if (cartItem.quantity < 10) {
                        cartItem.quantity++
                        cartViewModel.updateCartItem(cartItem.copy(userId = userId))
                   //     Toast.makeText(context, "${cartItem.name} quantity increased", Toast.LENGTH_SHORT).show()
                    }
                }

                cartdel.setOnClickListener {
                    cartViewModel.deleteCartItem(cartItem.copy(userId = userId))
               //     Toast.makeText(context, "${cartItem.name} removed from cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}




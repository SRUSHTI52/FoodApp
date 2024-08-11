package com.example.foodapp.Fragment

import CartViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Context // Import for SharedPreferences
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.view.size
import com.example.foodapp.CartAdapter
import com.example.foodapp.PurchaseActivity
import com.example.foodapp.SpaceItemDecoration
import com.example.foodapp.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        //Toast.makeText(context, "cartfragment $userId", Toast.LENGTH_SHORT).show()


        cartViewModel.setUserId(userId)

        val adapter = CartAdapter(
            requireContext(),
            cartViewModel,
            userId
        )
       // Toast.makeText(context, "$adapter.getitemCount()", Toast.LENGTH_SHORT).show()

        binding.cartlist.layoutManager = LinearLayoutManager(requireContext())
        binding.cartlist.adapter = adapter
       // binding.cartlist.addItemDecoration(SpaceItemDecoration(15))
       // Toast.makeText(context, "cartfragment ${binding.cartlist.size}", Toast.LENGTH_SHORT).show()

        cartViewModel.cartItems.observe(viewLifecycleOwner) { updatedCartItems ->
            val userCartItems = updatedCartItems.filter { it.userId == userId }
            adapter.updateCartItems(userCartItems)
        }

        binding.cartbtn.setOnClickListener {
            val intent = Intent(requireContext(), PurchaseActivity::class.java)
            intent.putExtra("user_id", userId) // Pass the user ID to the PurchaseActivity
            startActivity(intent)
        }
    }
}



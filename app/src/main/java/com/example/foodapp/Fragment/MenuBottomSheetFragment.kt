package com.example.foodapp.Fragment

import CartViewModel
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.CartDatabase
import com.example.foodapp.CartItem
import com.example.foodapp.HomeAdapter
import com.example.foodapp.Item
import com.example.foodapp.ItemDao
import com.example.foodapp.MenuAdapter
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Array

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var itemDao: ItemDao
    private lateinit var binding: FragmentMenuBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)
        itemDao = CartDatabase.getDatabase(requireContext()).itemDao()


        itemDao.getAllItems().observe(viewLifecycleOwner, Observer { items ->
            val menuNames = items.map { it.name }.toMutableList()
//            val foodPrices = items.map { "Rs. ${it.price}" }
            val menuPrices = items.map {it.price}.toMutableList()
            val menuImages = items.map { it.imageUrl }.toMutableList()
            val sharedPreferences = context?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val uId = sharedPreferences?.getInt("user_id", -1)
            val adapter = HomeAdapter(menuNames, menuPrices, menuImages,uId) { name, price, imageResource, uId ->
                val cartItem = CartItem(name = name, price = price, imageResource = imageResource, userId = uId,quantity = 1)
                cartViewModel.addCartItem(cartItem)
            }

            binding.menurec.layoutManager = LinearLayoutManager(requireContext())
            binding.menurec.adapter = adapter
        })

        binding.menuback.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
    private fun deleteFirstFourItems() {
        CoroutineScope(Dispatchers.IO).launch {
            val firstFourItems = itemDao.getFirstFourItems()
            if (firstFourItems.isNotEmpty()) {
                itemDao.deleteItems(firstFourItems)
            }
        }
    }
    companion object {

    }
}
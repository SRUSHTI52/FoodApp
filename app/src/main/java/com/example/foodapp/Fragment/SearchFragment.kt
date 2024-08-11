package com.example.foodapp.Fragment

import CartViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.CartDatabase
import com.example.foodapp.CartItem
import com.example.foodapp.Item
import com.example.foodapp.ItemDao
import com.example.foodapp.MenuAdapter
import com.example.foodapp.R
import com.example.foodapp.databinding.SearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private lateinit var binding: SearchBinding
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var itemDao: ItemDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchBinding.inflate(inflater, container, false)

        val searchTextView = binding.searchbar.findViewById<androidx.appcompat.widget.SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)

        searchTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown))

        searchTextView.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.brown))

         itemDao = CartDatabase.getDatabase(requireContext()).itemDao()
        binding.searchrec.layoutManager = LinearLayoutManager(requireContext())


        binding.searchbar.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchItems(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchItems(it) }
                return true
            }
        })

        return binding.root
    }

    private fun searchItems(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val items = itemDao.searchItems("%$query%")
            withContext(Dispatchers.Main) {
                if (items.isEmpty()) {

                    binding.noResultsText.visibility = View.VISIBLE
                    binding.searchrec.visibility = View.GONE
                }
                else{

                    binding.noResultsText.visibility = View.GONE
                    binding.searchrec.visibility = View.VISIBLE

                    val menuNames = items.map { it.name }
                        val menuPrices = items.map { it.price }
                        val menuImages = items.map { it.imageUrl }
                    val sharedPreferences = context?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    val uId = sharedPreferences?.getInt("user_id", -1)
                        val adapter = uId?.let {
                            MenuAdapter(
                                menuNames.toMutableList(),
                                menuPrices.toMutableList(),
                                menuImages.toMutableList(),
                                it
                            ) { name, price, imageResource, uId ->
                                val cartItem = CartItem(
                                    name = name,
                                    price = price,
                                    imageResource = imageResource,
                                    userId = uId,
                                    quantity = 1
                                )
                                cartViewModel.addCartItem(cartItem)
                            }
                        }

                        binding.searchrec.layoutManager = LinearLayoutManager(requireContext())
                        binding.searchrec.adapter = adapter
                    }
            }
        }
    }
}


package com.example.foodapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodapp.CartDatabase
import com.example.foodapp.CartItem
import com.example.foodapp.HomeAdapter
import CartViewModel
import android.content.Context
import com.example.foodapp.Item
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.ItemDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var itemDao: ItemDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewmenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemDao = CartDatabase.getDatabase(requireContext()).itemDao()


        populateDatabase()


        itemDao.getAllItems().observe(viewLifecycleOwner, Observer { items ->
            val foodNames = items.map { it.name }
//            val foodPrices = items.map { "Rs. ${it.price}" }
            val foodPrices = items.map {it.price}
            val foodImages = items.map { it.imageUrl }
            val sharedPreferences = context?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val uId = sharedPreferences?.getInt("user_id", -1)


            val adapter = HomeAdapter(foodNames, foodPrices, foodImages,uId) { name, price, imageResource, uId ->
                val cartItem = CartItem(name = name, price = price, imageResource = imageResource, userId = uId,quantity = 1)
                cartViewModel.addCartItem(cartItem)
            }

            binding.itemlist.layoutManager = LinearLayoutManager(requireContext())
            binding.itemlist.adapter = adapter
        })

        val imagelist = ArrayList<SlideModel>()
        imagelist.add(SlideModel(R.drawable.image1, ScaleTypes.FIT))
        imagelist.add(SlideModel(R.drawable.image2, ScaleTypes.FIT))
        imagelist.add(SlideModel(R.drawable.image3, ScaleTypes.FIT))

        val imageSlider = binding.imageslider
        imageSlider.setImageList(imagelist, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {

            }

            override fun onItemSelected(position: Int) {

            }
        })
    }

    private fun populateDatabase() {
        CoroutineScope(Dispatchers.IO).launch {

            if (itemDao.getAllItems().value.isNullOrEmpty()) {

                itemDao.insertItem(Item(name = "Burger", price = 59.0,  imageUrl = R.drawable.burger))
                itemDao.insertItem(Item(name = "Pancake", price = 150.0, imageUrl = R.drawable.pancake))
            }
        }
    }
}

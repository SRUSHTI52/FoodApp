package com.example.foodapp.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.CartDatabase
import com.example.foodapp.OrderHistoryAdapter
import com.example.foodapp.OrderHistoryDao
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentOrderHistoryBinding

class OrderHistoryFragment : Fragment() {
    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var orderHistoryDao: OrderHistoryDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderHistoryDao = CartDatabase.getDatabase(requireContext()).orderHistoryDao()

        val sharedPreferences = context?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val uId = sharedPreferences?.getInt("user_id", -1)

        if (uId != null && uId != -1) {
            orderHistoryDao.getAllOrders(uId).observe(viewLifecycleOwner, Observer { orders ->
                val adapter = OrderHistoryAdapter(orders)
                binding.orderHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.orderHistoryRecyclerView.adapter = adapter
            })
        }
        else
            Toast.makeText(context, "orderhistoryfragment", Toast.LENGTH_SHORT).show()
    }
}


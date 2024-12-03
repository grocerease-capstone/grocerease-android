package com.exal.grocerease.view.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.exal.grocerease.databinding.FragmentHomeBinding
import com.exal.grocerease.helper.rupiahFormatter
import com.exal.grocerease.viewmodel.HomeViewModel
import com.exal.grocerease.R
import com.exal.grocerease.view.adapter.RecentlyAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private var totalPrice = 0
    private var totalItem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        // Tampilkan total price dan total items ke UI
        binding.total.text = rupiahFormatter(totalPrice)
        binding.items.text = "$totalItem Items"
    }

    private fun setupRecyclerView() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class Item(
    val itemImage: Int,
    val itemPrice: Int,
    val itemTotal: Int,
    val itemDate: String
)

package com.example.sparklehaven.dashboard.navigation_fragments.profile.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.profile.activities.ShippingDetailsActivity
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters.ShippingDetailBottomSheetAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.view_model.ShippingDetailViewModel
import com.example.sparklehaven.databinding.FragmentShippingDetailBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShippingDetailBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentShippingDetailBottomSheetBinding
    private lateinit var shippingDetailBottomSheetAdapter : ShippingDetailBottomSheetAdapter

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: ShippingDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShippingDetailBottomSheetBinding.inflate(layoutInflater)

        // Load and Observe Shipping Details
        loadShippingDetails()
        observeShippingDetails()

        // Move to Shipping Details Screen
        moveToAddShippingDetails()

        return binding.root
    }

    private fun moveToAddShippingDetails() {
        binding.addNewShippingBtn.setOnClickListener {
            startActivity(Intent(context, ShippingDetailsActivity::class.java))
        }
    }

    private fun loadShippingDetails() {
        shippingDetailBottomSheetAdapter = ShippingDetailBottomSheetAdapter()
        binding.shippingAddressRecView.adapter = shippingDetailBottomSheetAdapter
        binding.shippingAddressRecView.layoutManager = LinearLayoutManager(context)
    }

    private fun observeShippingDetails() {
        viewModel.shippingDetailList.observe(viewLifecycleOwner) { shippingDetails ->
            shippingDetailBottomSheetAdapter.updateItems(shippingDetails)
        }
    }
}
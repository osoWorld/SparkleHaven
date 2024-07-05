package com.example.sparklehaven.dashboard.navigation_fragments.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters.SettingsItemAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.view_model.SettingsViewModel
import com.example.sparklehaven.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var upperSettingsAdapter: SettingsItemAdapter
    private lateinit var lowerSettingsAdapter: SettingsItemAdapter


    // Initialize ViewModel using viewModels() delegate
    private val viewModel : SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        setupRecyclerViews()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerViews() {
        upperSettingsAdapter = SettingsItemAdapter(requireContext(), childFragmentManager)
        lowerSettingsAdapter = SettingsItemAdapter(requireContext(),childFragmentManager)

        binding.upperSettingItemRecView.layoutManager = LinearLayoutManager(context)
        binding.upperSettingItemRecView.adapter = upperSettingsAdapter

        binding.lowerSettingItemsRecView.layoutManager = LinearLayoutManager(context)
        binding.lowerSettingItemsRecView.adapter = lowerSettingsAdapter
    }

    private fun observeViewModel() {
        viewModel.upperSettingListItems.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                Log.d("ProfileFragment", "upperSettingListItems received: $items")
                upperSettingsAdapter.updateItems(items)
            } else {
                Log.e("ProfileFragment", "upperSettingListItems is null")
            }
        }

        viewModel.lowerSettingListItems.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                Log.d("ProfileFragment", "lowerSettingListItems received: $items")
                lowerSettingsAdapter.updateItems(items)
            } else {
                Log.e("ProfileFragment", "lowerSettingListItems is null")
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.loadUpperSettingsItems()
        viewModel.loadLowerSettingsItems()
    }
}
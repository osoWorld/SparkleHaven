package com.example.sparklehaven.dashboard.navigation_fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.adapters.FavoriteItemAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model.FavoriteItemViewModel
import com.example.sparklehaven.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    private lateinit var favoriteItemAdapter: FavoriteItemAdapter

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: FavoriteItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)

        productItems()

        return binding.root
    }

    private fun productItems () {
        favoriteItemAdapter = FavoriteItemAdapter(requireContext())
        binding.favoriteRecView.adapter = favoriteItemAdapter
        binding.favoriteRecView.layoutManager = GridLayoutManager(context,2)

        viewModel.favoriteItemsList.observe(viewLifecycleOwner) { favItems ->
//            favoriteItemAdapter.updateItems(favItems)
        }
    }
}
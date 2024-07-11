package com.example.sparklehaven.dashboard.navigation_fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.adapters.FavoriteItemAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model.FavoriteViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.viewmodel_factory.FavoriteViewModelFactory
import com.example.sparklehaven.data.local.AppDatabase
import com.example.sparklehaven.data.repository.FavoriteRepository
import com.example.sparklehaven.databinding.FragmentFavoriteBinding
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    private lateinit var favoriteItemAdapter: FavoriteItemAdapter

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: FavoriteViewModel by viewModels {
        val userId = FirebaseModule.firebaseAuth.currentUser?.uid ?: ""
        FavoriteViewModelFactory(
            userId,
            FavoriteRepository(
                AppDatabase.getDatabase(requireContext()).favoriteDao(),
                FirebaseModule.firebaseFirestore
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)

        setupRecyclerView()

        // Observe favorites LiveData and update adapter
        viewModel.favoriteProducts.observe(viewLifecycleOwner) { favoriteProducts ->
            favoriteItemAdapter.updateItems(favoriteProducts)
        }

   //     viewModel.fetchFavorites() // Fetch favorites when the fragment is created

        return binding.root
    }

    private fun setupRecyclerView() {
        favoriteItemAdapter = FavoriteItemAdapter(requireContext(), viewModel)
        binding.favoriteRecView.adapter = favoriteItemAdapter
        binding.favoriteRecView.layoutManager = GridLayoutManager(context, 2)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
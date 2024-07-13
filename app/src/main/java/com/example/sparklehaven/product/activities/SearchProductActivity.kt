package com.example.sparklehaven.product.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.category.classes.view_model.CategoryDetailViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.adapters.FavoriteItemAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model.FavoriteViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.viewmodel_factory.FavoriteViewModelFactory
import com.example.sparklehaven.data.local.AppDatabase
import com.example.sparklehaven.data.repository.FavoriteRepository
import com.example.sparklehaven.databinding.ActivitySearchProductBinding
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar

class SearchProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchProductBinding
    private lateinit var favoriteItemAdapter: FavoriteItemAdapter

    private val userId = FirebaseModule.firebaseAuth.currentUser?.uid ?: ""

    private val viewModel: CategoryDetailViewModel by viewModels()
    private val favViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(
            userId,
            FavoriteRepository(
                AppDatabase.getDatabase(this@SearchProductActivity).favoriteDao(),
                FirebaseModule.firebaseFirestore
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProductBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupSearchBar()

        // Load all products initially
        viewModel.loadProducts("All Accessories")
        observeProducts()
    }

    private fun setupRecyclerView() {
        favoriteItemAdapter = FavoriteItemAdapter(this, favViewModel)
        binding.allProductRecView.adapter = favoriteItemAdapter
        binding.allProductRecView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun observeProducts() {
        viewModel.categoryDetailList.observe(this) { products ->
            favoriteItemAdapter.updateItems(products)
        }

        // Fetch and observe favorites
        favViewModel.fetchFavorites()

        viewModel.message.observe(this) { errorMessage ->
            showSnackbar(errorMessage)
        }

        viewModel.progress.observe(this) { progress ->
            binding.progressBar.visibility = if (progress) View.VISIBLE else View.GONE
        }
    }

    private fun setupSearchBar() {
        binding.allProductSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterProducts(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed
            }
        })
    }

    private fun filterProducts(query: String) {
        val filteredList = viewModel.categoryDetailList.value?.filter { product ->
            product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
        }
        favoriteItemAdapter.updateItems(filteredList ?: emptyList())
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
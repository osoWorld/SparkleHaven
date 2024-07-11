package com.example.sparklehaven.dashboard.navigation_fragments.category.activites

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.category.classes.view_model.CategoryDetailViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.adapters.FavoriteItemAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model.FavoriteViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.viewmodel_factory.FavoriteViewModelFactory
import com.example.sparklehaven.data.local.AppDatabase
import com.example.sparklehaven.data.repository.FavoriteRepository
import com.example.sparklehaven.databinding.ActivityCategoryDetailBinding
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar

class CategoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryDetailBinding
    private lateinit var favoriteItemAdapter: FavoriteItemAdapter
    private val userId = FirebaseModule.firebaseAuth.currentUser?.uid ?: ""

    // Initialize ViewModel using viewModels() delegate
       private val viewModel: CategoryDetailViewModel by viewModels()
    private val favViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(
            userId,
            FavoriteRepository(
                AppDatabase.getDatabase(this@CategoryDetailActivity).favoriteDao(),
                FirebaseModule.firebaseFirestore
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val title = intent.getStringExtra("categoryTitle")
        binding.categoryDetailTitle.text = title!!

        // Fetch products based on category title
        viewModel.loadProducts(title)

        // Load & observe CategoryItems
        loadCategoryDetailItems(title)
        observeCategoryDetailItems()
    }

    private fun loadCategoryDetailItems(categoryTitle: String) {
        favoriteItemAdapter = FavoriteItemAdapter(this, favViewModel)
        binding.categoryDetailRecView.adapter = favoriteItemAdapter
        binding.categoryDetailRecView.layoutManager = GridLayoutManager(this, 2)
        viewModel.loadProducts(categoryTitle)
    }

    private fun observeCategoryDetailItems() {
        viewModel.categoryDetailList.observe(this) { categoryDetailItems ->
            favoriteItemAdapter.updateItems(categoryDetailItems)
//            categoryDetailItems.forEach { Log.d("CategoryDetailActivity", "Product: ${it.name}, Category: ${it.category}") }
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

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
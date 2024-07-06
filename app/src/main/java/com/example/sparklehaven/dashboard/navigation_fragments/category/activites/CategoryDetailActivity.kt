package com.example.sparklehaven.dashboard.navigation_fragments.category.activites

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.category.classes.view_model.CategoryDetailViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.adapters.FavoriteItemAdapter
import com.example.sparklehaven.databinding.ActivityCategoryDetailBinding

class CategoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryDetailBinding
    private lateinit var favoriteItemAdapter: FavoriteItemAdapter

    // Initialize ViewModel using viewModels() delegate
       private val viewModel: CategoryDetailViewModel by viewModels()
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

        // Load & observe CategoryItems
        loadCategoryDetailItems()
        observeCategoryDetailItems()
    }

    private fun loadCategoryDetailItems () {
        favoriteItemAdapter = FavoriteItemAdapter(this)
        binding.categoryDetailRecView.adapter = favoriteItemAdapter
        binding.categoryDetailRecView.layoutManager = GridLayoutManager(this,2)

    }

    private fun observeCategoryDetailItems () {
        viewModel.categoryDetailList.observe(this) { categoryDetailItems ->
            favoriteItemAdapter.updateItems(categoryDetailItems)
        }
    }
}
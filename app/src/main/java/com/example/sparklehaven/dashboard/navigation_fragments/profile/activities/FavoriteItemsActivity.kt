package com.example.sparklehaven.dashboard.navigation_fragments.profile.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.adapters.FavoriteItemAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model.FavoriteItemViewModel
import com.example.sparklehaven.databinding.ActivityFavoriteItemsBinding

class FavoriteItemsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteItemsBinding
    private lateinit var favoriteItemAdapter: FavoriteItemAdapter

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: FavoriteItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteItemsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load FavItems
        productItems()


    }

    private fun productItems () {
        favoriteItemAdapter = FavoriteItemAdapter(this)
        binding.favoriteRecView.adapter = favoriteItemAdapter
        binding.favoriteRecView.layoutManager = GridLayoutManager(this,2)

//        viewModel.favoriteItemsList.observe(this) { favItems ->
//            favoriteItemAdapter.updateItems(favItems)
//        }
    }
}
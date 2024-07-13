package com.example.sparklehaven.dashboard.navigation_fragments.profile.activities

import android.content.Intent
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
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model.FavoriteViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.viewmodel_factory.FavoriteViewModelFactory
import com.example.sparklehaven.data.local.AppDatabase
import com.example.sparklehaven.data.repository.FavoriteRepository
import com.example.sparklehaven.databinding.ActivityFavoriteItemsBinding
import com.example.sparklehaven.product.activities.SearchProductActivity
import com.example.sparklehaven.utils.singleton.FirebaseModule

class FavoriteItemsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteItemsBinding
    private lateinit var favoriteItemAdapter: FavoriteItemAdapter

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: FavoriteViewModel by viewModels()
    private val favViewModel: FavoriteViewModel by viewModels {
        val userId = FirebaseModule.firebaseAuth.currentUser?.uid ?: ""
        FavoriteViewModelFactory(
            userId,
            FavoriteRepository(
                AppDatabase.getDatabase(this).favoriteDao(),
                FirebaseModule.firebaseFirestore
            )
        )
    }

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

        binding.searchView.apply {
            isFocusable = false
            isFocusableInTouchMode = false
            isClickable = true
            setOnClickListener {
                goToSearchProduct()
            }
        }

        // Load FavItems
        productItems()


    }

    private fun productItems () {
        favoriteItemAdapter = FavoriteItemAdapter(this, favViewModel)
        binding.favoriteRecView.adapter = favoriteItemAdapter
        binding.favoriteRecView.layoutManager = GridLayoutManager(this,2)

        // Observe favorites LiveData and update adapter
        viewModel.favoriteProducts.observe(this) { favoriteProducts ->
            favoriteItemAdapter.updateItems(favoriteProducts)
        }

        viewModel.fetchFavorites() // Fetch favorites when the fragment is created
    }

    private fun goToSearchProduct () {
        startActivity(Intent(this, SearchProductActivity::class.java))
    }
}
package com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparklehaven.Product
import com.example.sparklehaven.data.model.Favorite
import com.example.sparklehaven.data.repository.FavoriteRepository
import com.example.sparklehaven.utils.singleton.FirebaseModule
import kotlinx.coroutines.launch

class FavoriteViewModel(private val userId: String, private val repository: FavoriteRepository) : ViewModel() {
    private val _favoriteProducts = MutableLiveData<List<Product>>()
    val favoriteProducts: LiveData<List<Product>> get() = _favoriteProducts


    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            repository.toggleFavorite(userId, product)
            fetchFavorites()
        }
    }

    fun fetchFavorites() {
        viewModelScope.launch {
            val favorites = repository.fetchFavorites(userId)
            val productIds = favorites.map { it.productId }
            val favoriteProducts = repository.fetchProductsByIds(productIds)
            _favoriteProducts.value = favoriteProducts
        }
    }
}
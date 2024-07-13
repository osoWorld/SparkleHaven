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

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> get() = _progress

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            _progress.value = true
            repository.toggleFavorite(userId, product)
            fetchFavorites()    // Ensure the favorites are fetched after toggling
            _progress.value = false
        }
    }

    fun fetchFavorites() {
        viewModelScope.launch {
            _progress.value = true
            val favorites = repository.fetchFavorites(userId)
            val productIds = favorites.map { it.productId }
            val favoriteProducts = repository.fetchProductsByIds(productIds)
            _favoriteProducts.value = favoriteProducts
            _progress.value = false
        }
    }
}
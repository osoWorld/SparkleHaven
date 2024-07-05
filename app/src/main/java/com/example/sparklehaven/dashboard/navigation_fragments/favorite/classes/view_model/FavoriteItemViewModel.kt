package com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.HomeCategoryModel
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.ProductItemsModel

class FavoriteItemViewModel : ViewModel() {
    private val _favoriteItemsList = MutableLiveData<List<ProductItemsModel>>()
    val favoriteItemsList: LiveData<List<ProductItemsModel>> get() = _favoriteItemsList

    private val allFavoriteItems: List<ProductItemsModel> = listOf(
        ProductItemsModel(R.drawable.elegant_gold_quartz_necklace, "Elegant Gold Quartz Necklace", "230,000"),
        ProductItemsModel(R.drawable.elegant_asthetic_gold_pearl_earring, "Elegant Aesthetic Gold Pearl Earrings", "180,000"),
        ProductItemsModel(R.drawable.pure_gold_couple_ring, "Pure Gold Couple Ring", "181,000"),
        ProductItemsModel(R.drawable.pure_gold_bracelet, "Pure Gold Bracelet", "93,000"),
        ProductItemsModel(R.drawable.luxury_gold_men_antique_watch, "Luxury Gold Men Antique Watch", "250,000"),
    )

    init {
        loadFavoriteItems()
    }

    private fun loadFavoriteItems() {
        _favoriteItemsList.postValue(allFavoriteItems)
    }
}
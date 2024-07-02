package com.example.sparklehaven.dashboard.navigation_fragments.home.classes.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.HomeCategoryModel
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.ProductItemsModel

class HomeViewModel : ViewModel() {
    private val _categories = MutableLiveData<List<HomeCategoryModel>>()
    val categories: LiveData<List<HomeCategoryModel>> get() = _categories

    private val _products = MutableLiveData<List<ProductItemsModel>>()
    val products: LiveData<List<ProductItemsModel>> get() = _products

    private val allProductList: List<ProductItemsModel> = listOf(
        ProductItemsModel(R.drawable.elegant_gold_quartz_necklace, "Elegant Gold Quartz Necklace", "230,000"),
        ProductItemsModel(R.drawable.elegant_asthetic_gold_pearl_earring, "Elegant Aesthetic Gold Pearl Earring", "180,000"),
        ProductItemsModel(R.drawable.pure_gold_couple_ring, "Pure Gold Couple Ring", "181,000"),
        ProductItemsModel(R.drawable.pure_gold_bracelet, "Pure Gold Bracelet", "93,000"),
        ProductItemsModel(R.drawable.luxury_gold_men_antique_watch, "Luxury Gold Men Antique Watch", "250,000"),

        // Add all your products here with their respective categories
    )

    init {
        loadCategories()
        loadAllProducts()
    }

    private fun loadCategories() {
        _categories.value = listOf(
            HomeCategoryModel(R.drawable.accessory, "Accessory"),
            HomeCategoryModel(R.drawable.necklace_1, "Necklace"),
            HomeCategoryModel(R.drawable.earrings, "Earrings"),
            HomeCategoryModel(R.drawable.diamond_ring_icons, "Ring"),
            HomeCategoryModel(R.drawable.bracelet, "Bracelet"),
            HomeCategoryModel(R.drawable.brooch, "Brooch"),
            HomeCategoryModel(R.drawable.smart_watch, "Watch"),
            HomeCategoryModel(R.drawable.anklet, "Anklet"),
        )
    }

    private fun loadAllProducts() {
        _products.value = allProductList
    }

    fun filterProductsByCategories(category: String) {
        if (category == "Accessory") {
            loadAllProducts()
        } else {
            _products.value = allProductList.filter { it.productItemName.contains(category) }
        }
    }
}
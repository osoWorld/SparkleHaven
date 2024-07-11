package com.example.sparklehaven.dashboard.navigation_fragments.home.classes.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.Product
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.HomeCategoryModel
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.firebase.firestore.Source

class HomeViewModel : ViewModel() {
    private val _categories = MutableLiveData<List<HomeCategoryModel>>()
    val categories: LiveData<List<HomeCategoryModel>> get() = _categories

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = _messages

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val firestore = FirebaseModule.firebaseFirestore

    // Store the original list of products
    private var allProducts: List<Product> = emptyList()

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
            HomeCategoryModel(R.drawable.anklet, "Anklet"),
        )
    }

    private fun loadAllProducts() {
        _progress.postValue(true)
        firestore.collection("products")
            .get(Source.CACHE)
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    fetchProductsFromServer()
                } else {
                    val products = documents.map { it.toObject(Product::class.java) }
                    allProducts = products
                    _products.value = products
                    _progress.postValue(false)
                }
            }
            .addOnFailureListener {
                fetchProductsFromServer()
            }
    }

    private fun fetchProductsFromServer() {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                _progress.postValue(false)
                val products = documents.map { it.toObject(Product::class.java) }
                allProducts = products
                _products.value = products
            }
            .addOnFailureListener { exception ->
                _progress.postValue(false)
                _messages.postValue("Error getting documents: $exception")
            }
    }

    fun filterProductsByCategories(category: String) {
        if (category == "Accessory") {
            _products.value = allProducts
        } else {
            val filteredProducts = allProducts.filter {
                val regex = "\\b${Regex.escape(category)}\\b".toRegex(RegexOption.IGNORE_CASE)
                regex.containsMatchIn(it.name)
            }
            _products.value = filteredProducts
        }
    }
}
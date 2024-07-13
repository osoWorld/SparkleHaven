package com.example.sparklehaven.dashboard.navigation_fragments.category.classes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.Product
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.firebase.firestore.Source

class CategoryDetailViewModel : ViewModel() {
    private val _categoryDetailList = MutableLiveData<List<Product>>()
    val categoryDetailList: LiveData<List<Product>> = _categoryDetailList

    private val _messages = MutableLiveData<String>()
        val message: LiveData<String> = _messages

    private val _progress = MutableLiveData<Boolean>()
        val progress: LiveData<Boolean> = _progress

    private val firestore = FirebaseModule.firebaseFirestore

    private fun fetchProducts(categoryTitle: String) {
        _progress.postValue(true)
        firestore.collection("products")
            .get(Source.CACHE)
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    fetchProductsFromServer(categoryTitle)
                } else {
                    val products = documents.map { it.toObject(Product::class.java) }
                    filterProducts(products, categoryTitle)
                    _progress.postValue(false)
                }
            }
            .addOnFailureListener {
                fetchProductsFromServer(categoryTitle)
            }
    }

    private fun fetchProductsFromServer(categoryTitle: String) {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                _progress.postValue(false)
                val products = documents.map { it.toObject(Product::class.java) }
                filterProducts(products, categoryTitle)
            }
            .addOnFailureListener { exception ->
                _progress.postValue(false)
                _messages.postValue("Error getting documents: $exception")
            }
    }

    private fun filterProducts(products: List<Product>, categoryTitle: String) {
        val filteredProducts = if (categoryTitle == "All Accessories") {
            products
        } else {
            products.filter { it.name.contains(categoryTitle, ignoreCase = true) }
        }

        if (filteredProducts.isEmpty()) {
            _messages.postValue("No products found for this category.")
        }

        _categoryDetailList.value = filteredProducts
    }

    fun loadProducts(categoryTitle: String) {
        fetchProducts(categoryTitle)
    }
}
package com.example.sparklehaven.product.checkout.classes.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.R
import com.example.sparklehaven.product.add_to_cart.classes.model.CartItemsModel

class CheckoutViewModel : ViewModel () {
    private val _checkoutItemsList = MutableLiveData<List<CartItemsModel>> ()
    val checkoutItemsList : LiveData<List<CartItemsModel>> get() = _checkoutItemsList

    private val allCartItems : List<CartItemsModel> = listOf(
        CartItemsModel(R.drawable.elegant_asthetic_gold_pearl_earring, "Elegant Aesthetic Gold Pearl Earring", 25000, 1),
        CartItemsModel(R.drawable.pure_gold_couple_ring, "Pure Gold Couple Ring", 85000, 1),
        CartItemsModel(R.drawable.pure_gold_bracelet, "Pure Gold Bracelet", 25000, 1),
        CartItemsModel(R.drawable.elegant_gold_quartz_necklace, "Elegant Gold Quartz Necklace", 25000, 1),
    )

    init {
        loadCartItems()
        Log.d("AddToCartViewModel", "Initializing ViewModel")
    }

    private fun loadCartItems() {
        if (allCartItems.isNotEmpty()) {
            _checkoutItemsList.postValue(allCartItems)
            Log.d("AddToCartViewModel", "Loading cart items: $allCartItems")
        } else {
            Log.e("AddToCartViewModel", "allCartItems is empty")
        }
    }
}
package com.example.sparklehaven.product.checkout.classes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.data.model.CartItem

class CheckoutViewModel : ViewModel () {
    private val _checkoutItemsList = MutableLiveData<List<CartItem>>()
    val checkoutItemsList: LiveData<List<CartItem>> get() = _checkoutItemsList

    fun setCartItems(cartItems: List<CartItem>) {
        _checkoutItemsList.postValue(cartItems)
    }
}
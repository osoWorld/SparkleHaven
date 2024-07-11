package com.example.sparklehaven.product.add_to_cart.classes.view_model

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.Product
import com.example.sparklehaven.R
import com.example.sparklehaven.data.model.CartItem
import com.example.sparklehaven.product.add_to_cart.classes.model.CartItemsModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddToCartViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("cart_preferences", Context.MODE_PRIVATE)
    private val _cartItemsList = MutableLiveData<MutableList<CartItem>>()
    val cartItemsList: LiveData<MutableList<CartItem>> get() = _cartItemsList

    private val _subTotal = MutableLiveData<Double>()
    val subTotal: LiveData<Double> get() = _subTotal

    private val _shippingTotal = MutableLiveData<Double>()
    val shippingTotal: LiveData<Double> get() = _shippingTotal

    private val _grandTotal = MutableLiveData<Double>()
    val grandTotal: LiveData<Double> get() = _grandTotal

    private val _itemTotal = MutableLiveData<Int>()
    val itemTotal: LiveData<Int> get() = _itemTotal

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        val cartItemsJson = sharedPreferences.getString("cart_items", null)
        val cartItems: MutableList<CartItem> = if (cartItemsJson != null) {
            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            Gson().fromJson(cartItemsJson, type)
        } else {
            mutableListOf()
        }
        _cartItemsList.value = cartItems
        calculateTotals()
    }

    private fun saveCartItems(cartItems: List<CartItem>) {
        val editor = sharedPreferences.edit()
        val cartItemsJson = Gson().toJson(cartItems)
        editor.putString("cart_items", cartItemsJson)
        editor.apply()
    }

    fun updateCartItemCount(cartItem: CartItem, count: Int, totalPrice: Double) {
        val cartItems = _cartItemsList.value ?: mutableListOf()
        val existingCartItem = cartItems.find { it.productId == cartItem.productId }
        if (existingCartItem != null) {
            existingCartItem.productCount = count
            existingCartItem.totalPrice = totalPrice
        }
        _cartItemsList.value = cartItems
        saveCartItems(cartItems)
        calculateTotals()
    }

    fun removeCartItem(cartItem: CartItem) {
        val cartItems = _cartItemsList.value ?: mutableListOf()
        cartItems.removeAll { it.productId == cartItem.productId }
        _cartItemsList.value = cartItems
        saveCartItems(cartItems)
        calculateTotals()
    }

    private fun calculateTotals() {
        val cartItems = _cartItemsList.value ?: mutableListOf()
        val subTotal = cartItems.sumOf { it.totalPrice }
        val shippingTotal = subTotal * 0.05
        val grandTotal = subTotal + shippingTotal
        val itemTotal = cartItems.size

        _subTotal.value = subTotal
        _shippingTotal.value = shippingTotal
        _grandTotal.value = grandTotal
        _itemTotal.value = itemTotal
    }
}
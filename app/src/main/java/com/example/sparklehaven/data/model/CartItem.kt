package com.example.sparklehaven.data.model

data class CartItem(
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String,
    var productCount: Int,
    var totalPrice: Double
)

package com.example.sparklehaven.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String,
    var productCount: Int,
    var totalPrice: Double
) : Parcelable

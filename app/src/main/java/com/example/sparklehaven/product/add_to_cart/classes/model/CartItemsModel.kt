package com.example.sparklehaven.product.add_to_cart.classes.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItemsModel(
    val itemIcon: Int,
    val itemName: String,
    val itemPrice: Double,
    var itemCount: Int
) : Parcelable

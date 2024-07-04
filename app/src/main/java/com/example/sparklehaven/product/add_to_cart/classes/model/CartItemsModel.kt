package com.example.sparklehaven.product.add_to_cart.classes.model

data class CartItemsModel(
    val itemIcon : Int,
    val itemName : String,
    val itemPrice : Int,
    var itemCount : Int = 1
)

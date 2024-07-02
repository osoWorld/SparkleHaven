package com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model

data class ProductItemsModel (
    val productItemImage : Int,
    val productItemName : String,
    val productItemPrice : String,
    var isFavorite : Boolean = false
)

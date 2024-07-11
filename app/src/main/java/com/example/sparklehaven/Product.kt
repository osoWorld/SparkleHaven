package com.example.sparklehaven

import java.io.Serializable

data class Product(
    val name: String = "",
    val price: String = "",
    val category: String = "",
    val description: String = "",
    val imageUrl: String = "",
    var productId : String = "",
    var productRating : String = ""
) : Serializable

package com.example.sparklehaven.product.checkout.classes.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val postalCode: String = "",
    val state: String = "",
    val country: String = ""
) : Parcelable

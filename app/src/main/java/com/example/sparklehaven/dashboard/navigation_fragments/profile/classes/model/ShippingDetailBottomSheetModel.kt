package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model

data class ShippingDetailBottomSheetModel(
    val shippingDetailUserName: String,
    val shippingDetailPhoneNumber: String,
    val shippingDetailEmail: String,
    val shippingDetailAddress: String,
    var isChecked : Boolean = false
)

package com.example.sparklehaven.product.checkout.classes.model

data class PaymentMethodBottomSheetModel(
    val paymentMethodIcon : Int,
    val paymentMethodName : String,
    var isSelected: Boolean = false
)

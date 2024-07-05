package com.example.sparklehaven.product.checkout.classes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.R
import com.example.sparklehaven.product.checkout.classes.model.PaymentMethodBottomSheetModel

class PaymentMethodBottomSheetViewModel : ViewModel () {
    private val _paymentMethodList = MutableLiveData<List<PaymentMethodBottomSheetModel>>()
    val paymentMethodList : LiveData<List<PaymentMethodBottomSheetModel>> get() = _paymentMethodList

    private val allPaymentMethods : List<PaymentMethodBottomSheetModel> = listOf(
        PaymentMethodBottomSheetModel(R.drawable.visa, "Visa"),
        PaymentMethodBottomSheetModel(R.drawable.mastercard, "MasterCard"),
        PaymentMethodBottomSheetModel(R.drawable.bank_alfalah, "Bank Alfalah"),
        PaymentMethodBottomSheetModel(R.drawable.easypaisa, "Easypaisa"),
        PaymentMethodBottomSheetModel(R.drawable.nayapay, "Nayapay"),
        PaymentMethodBottomSheetModel(R.drawable.hbl, "HBL"),
        PaymentMethodBottomSheetModel(R.drawable.jazzcash, "JazzCash"),
        PaymentMethodBottomSheetModel(R.drawable.meezan_bank, "Meezan Bank"),
    )

    init {
        loadPaymentMethods()
    }

    private fun loadPaymentMethods() {
        _paymentMethodList.value = allPaymentMethods
    }
}
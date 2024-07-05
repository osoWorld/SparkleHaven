package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.ShippingDetailBottomSheetModel

class ShippingDetailViewModel : ViewModel() {
    private val _shippingDetailList = MutableLiveData<List<ShippingDetailBottomSheetModel>>()
    val shippingDetailList: LiveData<List<ShippingDetailBottomSheetModel>> = _shippingDetailList

    private val allShippingDetails : List<ShippingDetailBottomSheetModel> = listOf(
        ShippingDetailBottomSheetModel("Muhammad Suffian", "+92 303 6292862", "miansuffian74@gmail.com", "X-421, Eden Orchard, Lasani Puli, Faisalabad, Pakistan"),
        ShippingDetailBottomSheetModel("Muhammad Suffian", "+92 303 6292862", "miansuffian74@gmail.com", "X-421, Eden Orchard, Lasani Puli, Faisalabad, Pakistan"),
        ShippingDetailBottomSheetModel("Muhammad Suffian", "+92 303 6292862", "miansuffian74@gmail.com", "X-421, Eden Orchard, Lasani Puli, Faisalabad, Pakistan"),
        ShippingDetailBottomSheetModel("Muhammad Suffian", "+92 303 6292862", "miansuffian74@gmail.com", "X-421, Eden Orchard, Lasani Puli, Faisalabad, Pakistan"),
        ShippingDetailBottomSheetModel("Muhammad Suffian", "+92 303 6292862", "miansuffian74@gmail.com", "X-421, Eden Orchard, Lasani Puli, Faisalabad, Pakistan"),
        ShippingDetailBottomSheetModel("Muhammad Suffian", "+92 303 6292862", "miansuffian74@gmail.com", "X-421, Eden Orchard, Lasani Puli, Faisalabad, Pakistan"),
    )

    init {
        loadShippingDetails()
    }

    private fun loadShippingDetails() {
        _shippingDetailList.postValue(allShippingDetails)
    }
}
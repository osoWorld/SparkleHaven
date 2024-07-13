package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.ShippingDetailBottomSheetModel
import com.example.sparklehaven.product.checkout.classes.model.Address
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.firebase.firestore.Source

class ShippingDetailViewModel : ViewModel() {
    private val firestore = FirebaseModule.firebaseFirestore
    private val auth = FirebaseModule.firebaseAuth
    private val currentUser: String = auth.currentUser?.uid ?: ""

    private val _shippingDetailList = MutableLiveData<List<Address>>()
    val shippingDetailList: LiveData<List<Address>> = _shippingDetailList

    private val _emptyState = MutableLiveData<Boolean>()
    val emptyState: LiveData<Boolean> get() = _emptyState

    init {
        loadShippingAddressesFromCache()
    }

    private fun loadShippingAddressesFromCache() {
        firestore.collection("ShippingAddress").document(currentUser).collection("addresses")
            .get(Source.CACHE)
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val addresses = documents.map { it.toObject(Address::class.java) }
                    _shippingDetailList.value = addresses
                    _emptyState.value = addresses.isEmpty()
                } else {
                    _emptyState.value = true
                }
            }
            .addOnFailureListener { e ->
                _emptyState.value = true
                Log.e("ShippingDetailViewModel", "Error loading addresses from cache", e)
            }
    }
}
package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.HomeCategoryModel

class SettingsViewModel : ViewModel() {
    private val _upperSettingItemsList = MutableLiveData<List<HomeCategoryModel>>()
    val upperSettingListItems: LiveData<List<HomeCategoryModel>> get() = _upperSettingItemsList

    private val _lowerSettingItemsList = MutableLiveData<List<HomeCategoryModel>>()
    val lowerSettingListItems: LiveData<List<HomeCategoryModel>> get() = _lowerSettingItemsList

    init {
        Log.d("SettingsViewModel", "Initializing ViewModel")
        loadUpperSettingsItems()
        loadLowerSettingsItems()
    }

    private val upperSettingItems: List<HomeCategoryModel> = listOf(
        HomeCategoryModel(R.drawable.user, "Profile"),
//        HomeCategoryModel(R.drawable.paper_plane, "Message"),
        HomeCategoryModel(R.drawable.bags_shopping, "My Order"),
        HomeCategoryModel(R.drawable.heart, "Favorites"),
        HomeCategoryModel(R.drawable.truck_side, "Shipping Address"),
        HomeCategoryModel(R.drawable.cvv_card, "My Card")
    )

    private val lowerSettingItems: List<HomeCategoryModel> = listOf(
        HomeCategoryModel(R.drawable.faq, "FAQ"),
        HomeCategoryModel(R.drawable.shield_check, "Privacy Policy"),
//        HomeCategoryModel(R.drawable.feedback_review, "Ratings"),
        HomeCategoryModel(R.drawable.interrogation, "Help"),
    )

    fun loadUpperSettingsItems() {
        Log.d("SettingsViewModel", "Loading upper settings items")
        _upperSettingItemsList.postValue(upperSettingItems)
    }

    fun loadLowerSettingsItems() {
        Log.d("SettingsViewModel", "Loading lower settings items")
        _lowerSettingItemsList.postValue(lowerSettingItems)
    }
}
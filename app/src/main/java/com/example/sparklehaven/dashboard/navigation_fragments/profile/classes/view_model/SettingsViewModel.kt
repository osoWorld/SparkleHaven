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
        HomeCategoryModel(R.drawable.profile, "Profile"),
        HomeCategoryModel(R.drawable.message, "Message"),
        HomeCategoryModel(R.drawable.shopping_bag, "My Order"),
        HomeCategoryModel(R.drawable.unfav_icon, "Favorites"),
        HomeCategoryModel(R.drawable.shipping_address, "Shipping Address"),
        HomeCategoryModel(R.drawable.credit_card, "My Card"),
        HomeCategoryModel(R.drawable.setting, "Settings"),
    )

    private val lowerSettingItems: List<HomeCategoryModel> = listOf(
        HomeCategoryModel(R.drawable.faq, "FAQ"),
        HomeCategoryModel(R.drawable.privacy_policy, "Privacy Policy"),
        HomeCategoryModel(R.drawable.rate_star, "Ratings"),
        HomeCategoryModel(R.drawable.help, "Help"),
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
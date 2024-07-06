package com.example.sparklehaven.dashboard.navigation_fragments.category.classes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.category.classes.model.CategoryModel

class CategoryViewModel : ViewModel() {
    private val _categoryList = MutableLiveData<List<CategoryModel>>()
    val categoryList: LiveData<List<CategoryModel>> = _categoryList

    private val allCategoryList : List<CategoryModel> = listOf(
        CategoryModel("All Accessories", R.drawable.accessory_cat, "32"),
        CategoryModel("Diamond Ring", R.drawable.diamond_ring_cat, "32"),
        CategoryModel("Couple Ring", R.drawable.couple_ring_cat, "32"),
        CategoryModel("Gold Ring", R.drawable.gold_ring_cat, "32"),
        CategoryModel("Necklace", R.drawable.necklace_cat, "32"),
        CategoryModel("Bracelet", R.drawable.bracelet_cat, "32"),
        CategoryModel("Earring", R.drawable.earrings_cat, "32"),
        CategoryModel("Anklet", R.drawable.anklet_cat, "32"),
    )

    init {
        loadCategory()
    }

    private fun loadCategory() {
        _categoryList.postValue(allCategoryList)
    }
}
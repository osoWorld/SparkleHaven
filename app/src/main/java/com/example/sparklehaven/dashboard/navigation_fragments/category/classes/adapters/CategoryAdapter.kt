package com.example.sparklehaven.dashboard.navigation_fragments.category.classes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.dashboard.navigation_fragments.category.classes.model.CategoryModel
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.ShippingDetailBottomSheetModel
import com.example.sparklehaven.databinding.CategoryLayoutBinding

class CategoryAdapter (private val context: Context) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var categoryList : List<CategoryModel> = listOf()
    class CategoryViewHolder (val binding: CategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding.apply {
            categoryName.text = category.categoryName
            categoryCount.text = category.categoryCount
            categoryImage.setImageResource(category.categoryImage)
        }
    }

    fun updateItems(newItems: List<CategoryModel>) {
        categoryList = newItems
        notifyDataSetChanged()
    }
}
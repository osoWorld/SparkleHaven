package com.example.sparklehaven.dashboard.navigation_fragments.home.classes.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.HomeCategoryModel
import com.example.sparklehaven.databinding.HomeCategoryRcvBinding

class HomeCategoryAdapter(private val homeCategoryList: ArrayList<HomeCategoryModel>, private val onCategoryClick: (String) -> Unit) :
    ListAdapter<HomeCategoryModel, HomeCategoryAdapter.HomeCategoryViewModel>(CategoryDiffCallback()) {

    // private var selectedPosition = -1  // Track the selected position

    private var selectedPosition: Int = homeCategoryList.indexOfFirst { it.title == "Accessory" } // Default selected position

    class HomeCategoryViewModel(val binding: HomeCategoryRcvBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryViewModel {
        return HomeCategoryViewModel(
            HomeCategoryRcvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = homeCategoryList.size

    override fun onBindViewHolder(holder: HomeCategoryViewModel, position: Int) {
       // var isSelected = false

        val isSelected = position == selectedPosition  // Check if the current item is selected

        holder.binding.apply {
            homeCategoryImage.setImageResource(homeCategoryList[position].icon)
            homeCategoryTitle.text = homeCategoryList[position].title
        }

        // Update UI based on selection state
        if (isSelected) {
            holder.binding.homeCategoryCV.cardElevation = 10f
            holder.binding.selectedCategoryLayout.setBackgroundResource(R.drawable.selected_category)
            holder.binding.homeCategoryImage.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
            holder.binding.homeCategoryTitle.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        } else {
            holder.binding.homeCategoryCV.cardElevation = 3f
            holder.binding.selectedCategoryLayout.setBackgroundResource(R.drawable.unselected_category)
            holder.binding.homeCategoryImage.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
            holder.binding.homeCategoryTitle.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = if (selectedPosition == position) -1 else position  // Toggle selection

            // Notify the adapter to update both the newly selected and previously selected items
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onCategoryClick(homeCategoryList[position].title)
        }
    }

    // Optional: Method to update data in adapter
    fun updateCategories(newCategories: List<HomeCategoryModel>) {
        homeCategoryList.clear()
        homeCategoryList.addAll(newCategories)
        notifyDataSetChanged()
    }

    // Implement your CategoryDiffCallback for efficient updates
    private class CategoryDiffCallback : DiffUtil.ItemCallback<HomeCategoryModel>() {
        override fun areItemsTheSame(oldItem: HomeCategoryModel, newItem: HomeCategoryModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HomeCategoryModel, newItem: HomeCategoryModel): Boolean {
            return oldItem == newItem
        }
    }
}
package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.HomeCategoryModel
import com.example.sparklehaven.dashboard.navigation_fragments.profile.activities.ProfileActivity
import com.example.sparklehaven.databinding.SettingsItemLayoutBinding

class SettingsItemAdapter (private val context: Context) : RecyclerView.Adapter<SettingsItemAdapter.SettingsItemViewHolder>() {

    private var settingsItemList: List<HomeCategoryModel> = listOf()
    class SettingsItemViewHolder (val binding: SettingsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemViewHolder {
        return SettingsItemViewHolder(
            SettingsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = settingsItemList.size

    override fun onBindViewHolder(holder: SettingsItemViewHolder, position: Int) {
        val settingItems = settingsItemList[position]
        holder.binding.apply {
            settingsItemIcon.setImageResource(settingItems.icon)
            settingsItemName.text = settingItems.title
            if (position == settingsItemList.size) {
                greyUnderline.visibility = View.INVISIBLE
            }
        }

        holder.itemView.setOnClickListener {
            if (settingItems.title == "Profile") {
                val intent = Intent(context, ProfileActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
    fun updateItems(newItems: List<HomeCategoryModel>) {
        settingsItemList = newItems
        notifyDataSetChanged()
    }
}
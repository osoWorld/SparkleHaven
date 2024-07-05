package com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.ProductItemsModel
import com.example.sparklehaven.databinding.ItemsLayoutBinding
import com.example.sparklehaven.product.activities.ProductDetailsActivity

class FavoriteItemAdapter (private val context: Context) : RecyclerView.Adapter<FavoriteItemAdapter.FavoriteItemViewHolder> (){

    private var favoriteItemsList : List<ProductItemsModel> = listOf()

    class FavoriteItemViewHolder (val binding: ItemsLayoutBinding) : RecyclerView.ViewHolder (binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteItemViewHolder {
        return FavoriteItemViewHolder(
            ItemsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = favoriteItemsList.size

    override fun onBindViewHolder(holder: FavoriteItemViewHolder, position: Int) {
        val favoriteItem = favoriteItemsList[position]

        holder.binding.apply {
            itemProductImage.setImageResource(favoriteItem.productItemImage)
            itemProductName.text = favoriteItem.productItemName
            itemProductPrice.text = favoriteItem.productItemPrice

            // Update Favorite Icon based on User Clicked
            isFavoriteIcon.setImageResource(
                if (favoriteItem.isFavorite) {
                    R.drawable.fav_icon
                } else {
                    R.drawable.unfav_icon
                }
            )
        }

        holder.binding.favoriteIconBtn.setOnClickListener {
            favoriteItem.isFavorite = !favoriteItem.isFavorite
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ProductDetailsActivity::class.java))
        }
    }

    fun updateItems(newItems : List<ProductItemsModel>) {
        favoriteItemsList = newItems
        notifyDataSetChanged()
    }
}
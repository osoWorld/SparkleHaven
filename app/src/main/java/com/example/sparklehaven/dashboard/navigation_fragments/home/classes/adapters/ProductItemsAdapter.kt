package com.example.sparklehaven.dashboard.navigation_fragments.home.classes.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.model.ProductItemsModel
import com.example.sparklehaven.databinding.ItemsLayoutBinding
import com.example.sparklehaven.product.activities.ProductDetailsActivity

class ProductItemsAdapter (private val productItemsList: ArrayList<ProductItemsModel>, private val context: Context) : RecyclerView.Adapter<ProductItemsAdapter.ProductItemViewHolder>() {

    class ProductItemViewHolder(val binding: ItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        return ProductItemViewHolder(
            ItemsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int  {
        return minOf(productItemsList.size, 4)

        // Limit the item count to a maximum of 4
        // It does not, crashes the app even the list has less than 4 items
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {

        holder.binding.apply {
            itemProductImage.setImageResource(productItemsList[position].productItemImage)
            itemProductName.text = productItemsList[position].productItemName
            itemProductPrice.text = productItemsList[position].productItemPrice

            // Update Favorite Icon based on User Clicked
            isFavoriteIcon.setImageResource(
                if (productItemsList[position].isFavorite) {
                    R.drawable.fav_icon
                } else {
                    R.drawable.unfav_icon
                }
            )
        }

        holder.binding.favoriteIconBtn.setOnClickListener {
            productItemsList[position].isFavorite = !productItemsList[position].isFavorite
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ProductDetailsActivity::class.java))
        }
    }
    fun updateItems(newItems : List<ProductItemsModel>) {
        productItemsList.clear()
        productItemsList.addAll(newItems)
        notifyDataSetChanged()
    }

}
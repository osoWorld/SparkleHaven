package com.example.sparklehaven.dashboard.navigation_fragments.home.classes.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sparklehaven.Product
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model.FavoriteViewModel
import com.example.sparklehaven.databinding.ItemsLayoutBinding
import com.example.sparklehaven.product.activities.ProductDetailsActivity
import com.example.sparklehaven.utils.references.ExtrasRef

class ProductItemsAdapter (
    private val productItemsList: ArrayList<Product>,
    private val context: Context,
    private val favoriteViewModel: FavoriteViewModel
) : RecyclerView.Adapter<ProductItemsAdapter.ProductItemViewHolder>() {

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
        val product = productItemsList[position]
        holder.binding.apply {
            Glide.with(context).load(product.imageUrl).into(itemProductImage)
            itemProductName.text = product.name
            itemProductPrice.text = product.price

            // Update Favorite Icon based on User Clicked
            isFavoriteIcon.setImageResource(R.drawable.fav_icon)

            favoriteBtn.setOnClickListener {
                favoriteViewModel.toggleFavorite(product)
            }
        }

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ProductDetailsActivity::class.java)
                .putExtra(ExtrasRef.PRODUCT_DETAILS, product))
        }
    }
    fun updateItems(newItems : List<Product>) {
        productItemsList.clear()
        productItemsList.addAll(newItems)
        notifyDataSetChanged()
    }

}
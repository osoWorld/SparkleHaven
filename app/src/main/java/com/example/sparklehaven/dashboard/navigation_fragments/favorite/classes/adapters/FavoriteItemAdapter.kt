package com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.adapters

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

class FavoriteItemAdapter (
    private val context: Context,
    private val favoriteViewModel: FavoriteViewModel
) : RecyclerView.Adapter<FavoriteItemAdapter.FavoriteItemViewHolder> () {

    private var favoriteItemsList : List<Product> = listOf()

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
            Glide.with(context).load(favoriteItem.imageUrl).into(itemProductImage)
            itemProductName.text = favoriteItem.name
            itemProductPrice.text = favoriteItem.price

            favoriteViewModel.favoriteProducts.observe(context as LifecycleOwner) { favorites ->
                val isFavorite = favorites.any { it.productId == favoriteItem.productId }
                isFavoriteIcon.setImageResource(
                    if (isFavorite) R.drawable.fav_icon else R.drawable.unfav_icon
                )
            }

            favoriteBtn.setOnClickListener {
                favoriteViewModel.toggleFavorite(favoriteItem)
            }
        }

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ProductDetailsActivity::class.java)
                .putExtra(ExtrasRef.PRODUCT_DETAILS, favoriteItem))
        }
    }

    fun updateItems(newItems: List<Product>) {
        favoriteItemsList = newItems
        notifyDataSetChanged()
    }
}


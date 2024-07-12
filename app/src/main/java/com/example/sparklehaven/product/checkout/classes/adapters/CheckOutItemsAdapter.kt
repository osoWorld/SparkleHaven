package com.example.sparklehaven.product.checkout.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sparklehaven.data.model.CartItem
import com.example.sparklehaven.databinding.CartItemsLayoutBinding
import com.example.sparklehaven.databinding.CheckoutCartItemLayoutBinding
import com.example.sparklehaven.product.add_to_cart.classes.model.CartItemsModel

class CheckOutItemsAdapter : RecyclerView.Adapter<CheckOutItemsAdapter.CheckoutItemsViewHolder>() {

    private var cartItemsList : List<CartItem> = listOf()

    class CheckoutItemsViewHolder (val binding: CheckoutCartItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutItemsViewHolder {
        return CheckoutItemsViewHolder(
            CheckoutCartItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = cartItemsList.size

    override fun onBindViewHolder(holder: CheckoutItemsViewHolder, position: Int) {
        val cartItem = cartItemsList[position]
        holder.binding.apply {
            Glide.with(cartItemImage.context).load(cartItem.productImageUrl).into(cartItemImage)
            cartItemName.text = cartItem.productName
            cartItemPrice.text = cartItem.totalPrice.toString()
            productItemCount.text = cartItem.productCount.toString() // Display item count
        }
    }

    fun updateItems(newItems: List<CartItem>) {
        cartItemsList = newItems
        notifyDataSetChanged()
    }
}
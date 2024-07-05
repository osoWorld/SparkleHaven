package com.example.sparklehaven.product.checkout.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.databinding.CartItemsLayoutBinding
import com.example.sparklehaven.product.add_to_cart.classes.model.CartItemsModel

class CheckOutItemsAdapter : RecyclerView.Adapter<CheckOutItemsAdapter.CheckoutItemsViewHolder>() {

    private var cartItemsList : List<CartItemsModel> = listOf()

    class CheckoutItemsViewHolder (val binding: CartItemsLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutItemsViewHolder {
        return CheckoutItemsViewHolder(
            CartItemsLayoutBinding.inflate(
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
            cartItemImage.setImageResource(cartItem.itemIcon)
            cartItemName.text = cartItem.itemName
            cartItemPrice.text = cartItem.itemPrice.toString()
            counterGroup.visibility = View.GONE
        }
    }

    fun updateItems(newItems: List<CartItemsModel>) {
        cartItemsList = newItems
        notifyDataSetChanged()
    }
}
package com.example.sparklehaven.product.add_to_cart.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.databinding.CartItemsLayoutBinding
import com.example.sparklehaven.product.add_to_cart.classes.model.CartItemsModel

class CartItemsAdapter : RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>() {

    private var cartItemsList : List<CartItemsModel> = listOf()

    class CartItemsViewHolder (val binding: CartItemsLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsViewHolder {
        return CartItemsViewHolder(
            CartItemsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = cartItemsList.size

    override fun onBindViewHolder(holder: CartItemsViewHolder, position: Int) {
        val cartItem = cartItemsList[position]
        holder.binding.apply {
            cartItemImage.setImageResource(cartItem.itemIcon)
            cartItemName.text = cartItem.itemName
            cartItemPrice.text = cartItem.itemPrice.toString()
            cartItemCounter.text = cartItem.itemCount.toString()
        }


        // For incrementing & Decrementing of Products
        holder.binding.cartItemPlus.setOnClickListener {
            if (cartItem.itemCount in 1..99) {
                cartItem.itemCount ++
                holder.binding.cartItemCounter.text = cartItem.itemCount.toString()
            }
        }

        holder.binding.cartItemMinus.setOnClickListener {
            if (cartItem.itemCount > 1) {
                cartItem.itemCount --
                holder.binding.cartItemCounter.text = cartItem.itemCount.toString()
            }
        }
    }

    fun updateItems(newItems: List<CartItemsModel>) {
        cartItemsList = newItems
        notifyDataSetChanged()
    }
}
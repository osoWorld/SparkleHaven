package com.example.sparklehaven.product.add_to_cart.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sparklehaven.data.model.CartItem
import com.example.sparklehaven.databinding.CartItemsLayoutBinding
import com.example.sparklehaven.product.add_to_cart.classes.view_model.AddToCartViewModel

class CartItemsAdapter(private val viewModel: AddToCartViewModel) :
    RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>() {

    private var cartItems = mutableListOf<CartItem>()

    fun updateItems(items: List<CartItem>) {
        cartItems = items.toMutableList()
        notifyDataSetChanged()
    }

    class CartItemsViewHolder(val binding: CartItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsViewHolder {
        return CartItemsViewHolder(
            CartItemsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartItemsViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.binding.apply {
            Glide.with(root.context).load(cartItem.productImageUrl).into(cartItemImage)
            cartItemName.text = cartItem.productName
            cartItemPrice.text = cartItem.totalPrice.toString()
            cartItemCounter.text = cartItem.productCount.toString()

            // For incrementing & Decrementing of Products
            cartItemPlus.setOnClickListener {
                val newCount = cartItem.productCount + 1
                val newTotalPrice = cartItem.productPrice * newCount
                viewModel.updateCartItemCount(cartItem, newCount, newTotalPrice)
            }

            cartItemMinus.setOnClickListener {
                if (cartItem.productCount > 1) {
                    val newCount = cartItem.productCount - 1
                    val newTotalPrice = cartItem.productPrice * newCount
                    viewModel.updateCartItemCount(cartItem, newCount, newTotalPrice)
                } else {
                    AlertDialog.Builder(root.context)
                        .setMessage("Are you sure you want to remove this item from the cart?")
                        .setPositiveButton("Yes") { dialog, which ->
                            viewModel.removeCartItem(cartItem)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        }
    }

}
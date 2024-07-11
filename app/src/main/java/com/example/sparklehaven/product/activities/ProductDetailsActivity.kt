package com.example.sparklehaven.product.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.sparklehaven.Product
import com.example.sparklehaven.R
import com.example.sparklehaven.data.model.CartItem
import com.example.sparklehaven.databinding.ActivityProductDetailsBinding
import com.example.sparklehaven.utils.references.ExtrasRef
import com.example.sparklehaven.utils.singleton.SharedPreferencesModule
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var product : Product
    private var productCountText = 1
    private val cartPreferences: SharedPreferences by lazy {
        getSharedPreferences("cart_preferences", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent = intent
        product = intent.getSerializableExtra(ExtrasRef.PRODUCT_DETAILS) as Product

        binding.apply {
            Glide.with(this@ProductDetailsActivity)
                .load(product.imageUrl)
                .into(mainProductImage)

            productName.text = product.name
            productDescription.text = product.description
            itemProductPrice.text = product.price

            // Initialize the total price
            updateTotalPrice()

            plusBtn.setOnClickListener {
                productCountText ++
                updateTotalPrice()
            }

            minusBtn.setOnClickListener {
                if (productCountText > 1) {
                    productCountText --
                    updateTotalPrice()
                }
            }

            addToCartBtn.setOnClickListener {
                addToCart()
            }

        }
    }

    private fun updateTotalPrice() {
        val price = product.price.toDoubleOrNull() ?: 0.0
        val totalPrice = price * productCountText
        binding.productCount.text = productCountText.toString().padStart(2, '0')
        binding.productTotalPrice.text = totalPrice.toString()
    }

    private fun addToCart() {
        val price = product.price.toDoubleOrNull() ?: 0.0
        val totalPrice = price * productCountText

        val cartItem = CartItem(
            productId = product.productId,
            productName = product.name,
            productPrice = product.price.toDoubleOrNull() ?: 0.0,
            productImageUrl = product.imageUrl,
            productCount = productCountText,
            totalPrice = totalPrice
        )
        saveCartItem(cartItem)
        showSnackbar("Item added to cart")
    }

    private fun saveCartItem(cartItem: CartItem) {
        val cartItems = getCartItems().toMutableList()
        val existingItem = cartItems.find { it.productId == cartItem.productId }

        if (existingItem != null) {
            existingItem.productCount += cartItem.productCount
        } else {
            cartItems.add(cartItem)
        }

        val editor = cartPreferences.edit()
        editor.putString("cart_items", Gson().toJson(cartItems))
        editor.apply()
    }

    private fun getCartItems(): List<CartItem> {
        val cartItemsJson = cartPreferences.getString("cart_items", null) ?: return emptyList()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return Gson().fromJson(cartItemsJson, type)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
package com.example.sparklehaven.product.add_to_cart

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.view_models.HomeViewModel
import com.example.sparklehaven.data.model.CartItem
import com.example.sparklehaven.databinding.ActivityAddToCartBinding
import com.example.sparklehaven.product.add_to_cart.classes.adapters.CartItemsAdapter
import com.example.sparklehaven.product.add_to_cart.classes.view_model.AddToCartViewModel
import com.example.sparklehaven.product.checkout.CheckoutActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddToCartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddToCartBinding
    private lateinit var cartItemsAdapter : CartItemsAdapter

    private val cartPreferences: SharedPreferences by lazy {
        getSharedPreferences("cart_preferences", Context.MODE_PRIVATE)
    }

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: AddToCartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToCartBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadCartItem()
        observeViewModel()

        // Move to Checkout Screen
        proceedToCheckOut()
    }

    private fun loadCartItem() {
        cartItemsAdapter = CartItemsAdapter(viewModel)
        binding.cartItemsRecView.adapter = cartItemsAdapter
        binding.cartItemsRecView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.cartItemsList.observe(this) { cartItems ->
            if (cartItems != null) {
                cartItemsAdapter.updateItems(cartItems)
            }
        }

        viewModel.subTotal.observe(this) { subTotal ->
            binding.subTotalText.text = subTotal.toString()
        }

        viewModel.shippingTotal.observe(this) { shippingTotal ->
            binding.shippingTotalText.text = shippingTotal.toString()
        }

        viewModel.grandTotal.observe(this) { grandTotal ->
            binding.grandTotalText.text = grandTotal.toString()
        }

        viewModel.itemTotal.observe(this) { itemTotal ->
            binding.itemTotalText.text = "($itemTotal items)"
        }
    }

    private fun proceedToCheckOut() {
        binding.proceedToCheckoutButton.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }
}
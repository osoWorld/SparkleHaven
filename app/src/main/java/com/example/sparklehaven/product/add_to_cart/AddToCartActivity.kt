package com.example.sparklehaven.product.add_to_cart

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
import com.example.sparklehaven.databinding.ActivityAddToCartBinding
import com.example.sparklehaven.product.add_to_cart.classes.adapters.CartItemsAdapter
import com.example.sparklehaven.product.add_to_cart.classes.view_model.AddToCartViewModel

class AddToCartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddToCartBinding
    private lateinit var cartItemsAdapter : CartItemsAdapter

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
    }

    private fun loadCartItem() {
        cartItemsAdapter = CartItemsAdapter()
        binding.cartItemsRecView.adapter = cartItemsAdapter
        binding.cartItemsRecView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.cartItemsList.observe(this) { cartItems ->
            if (cartItems != null) {
                Log.d("AddToCartActivity", "Cart items received: $cartItems")
                cartItemsAdapter.updateItems(cartItems)
            } else {
                Log.e("AddToCartActivity", "cartItemsList is null")
            }
        }
    }
}
package com.example.sparklehaven.product.checkout

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.databinding.ActivityCheckoutBinding
import com.example.sparklehaven.product.add_to_cart.classes.adapters.CartItemsAdapter
import com.example.sparklehaven.product.add_to_cart.classes.view_model.AddToCartViewModel
import com.example.sparklehaven.product.checkout.classes.adapters.CheckOutItemsAdapter
import com.example.sparklehaven.product.checkout.classes.fragments.PaymentMethodBottomSheetFragment
import com.example.sparklehaven.product.checkout.classes.view_model.CheckoutViewModel

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCheckoutBinding
    private lateinit var checkOutItemsAdapter : CheckOutItemsAdapter

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadCartItem()
        observeViewModel()

        // Payment Methods
        paymentBottomSheet()

    }

    private fun loadCartItem() {
        checkOutItemsAdapter = CheckOutItemsAdapter()
        binding.checkoutCartItemsRecView.adapter = checkOutItemsAdapter
        binding.checkoutCartItemsRecView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.checkoutItemsList.observe(this) { cartItems ->
            if (cartItems != null) {
                Log.d("AddToCartActivity", "Cart items received: $cartItems")
                checkOutItemsAdapter.updateItems(cartItems)
            } else {
                Log.e("AddToCartActivity", "cartItemsList is null")
            }
        }
    }

    private fun paymentBottomSheet() {
        binding.paymentMethodCard.setOnClickListener {
            val bottomSheet = PaymentMethodBottomSheetFragment()
            bottomSheet.show(supportFragmentManager, "PaymentMethodBottomSheet")
        }
    }

}
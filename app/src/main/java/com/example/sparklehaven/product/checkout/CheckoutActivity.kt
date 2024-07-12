package com.example.sparklehaven.product.checkout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.data.model.CartItem
import com.example.sparklehaven.databinding.ActivityCheckoutBinding
import com.example.sparklehaven.product.add_to_cart.classes.adapters.CartItemsAdapter
import com.example.sparklehaven.product.add_to_cart.classes.model.CartItemsModel
import com.example.sparklehaven.product.add_to_cart.classes.view_model.AddToCartViewModel
import com.example.sparklehaven.product.checkout.classes.adapters.CheckOutItemsAdapter
import com.example.sparklehaven.product.checkout.classes.fragments.PaymentMethodBottomSheetFragment
import com.example.sparklehaven.product.checkout.classes.view_model.CheckoutViewModel
import com.example.sparklehaven.success.SuccessActivity

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

        // Receive cart items and totals from Intent
        val cartItemsList = intent.getParcelableArrayListExtra<CartItem>("cartItems")
//        val subTotal = intent.getDoubleExtra("subTotal", 0.0)
//        val shippingTotal = intent.getDoubleExtra("shippingTotal", 0.0)
        val grandTotal = intent.getDoubleExtra("grandTotal", 0.0)

        if (cartItemsList != null) {
            viewModel.setCartItems(cartItemsList)
        }

        binding.grandPriceText.text = grandTotal.toString()

        loadCartItem()
        observeViewModel()

        // Payment Methods
        paymentBottomSheet()

        // Place Order
        placeOrder()

    }

    private fun loadCartItem() {
        checkOutItemsAdapter = CheckOutItemsAdapter()
        binding.checkoutCartItemsRecView.adapter = checkOutItemsAdapter
        binding.checkoutCartItemsRecView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.checkoutItemsList.observe(this) { cartItems ->
            if (cartItems != null) {
                checkOutItemsAdapter.updateItems(cartItems)
            }
        }
    }

    private fun paymentBottomSheet() {
        binding.paymentMethodCard.setOnClickListener {
            val bottomSheet = PaymentMethodBottomSheetFragment()
            bottomSheet.show(supportFragmentManager, "PaymentMethodBottomSheet")
        }
    }

    private fun placeOrder () {
        binding.placeOrderButton.setOnClickListener {
            startActivity(Intent(this, SuccessActivity::class.java))
        }
    }
}
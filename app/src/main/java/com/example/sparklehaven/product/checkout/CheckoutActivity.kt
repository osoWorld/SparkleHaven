package com.example.sparklehaven.product.checkout

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.data.model.CartItem
import com.example.sparklehaven.databinding.ActivityCheckoutBinding
import com.example.sparklehaven.product.add_to_cart.classes.adapters.CartItemsAdapter
import com.example.sparklehaven.product.add_to_cart.classes.model.CartItemsModel
import com.example.sparklehaven.product.add_to_cart.classes.view_model.AddToCartViewModel
import com.example.sparklehaven.product.checkout.classes.adapters.CheckOutItemsAdapter
import com.example.sparklehaven.product.checkout.classes.fragments.PaymentMethodBottomSheetFragment
import com.example.sparklehaven.product.checkout.classes.fragments.ProgressDialogFragment
import com.example.sparklehaven.product.checkout.classes.model.Address
import com.example.sparklehaven.product.checkout.classes.view_model.CheckoutViewModel
import com.example.sparklehaven.success.SuccessActivity
import com.example.sparklehaven.utils.references.ExtrasRef
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var checkOutItemsAdapter: CheckOutItemsAdapter
    private val firestore = FirebaseModule.firebaseFirestore
    private val auth = FirebaseModule.firebaseAuth
    private val uid = auth.currentUser?.uid ?:""

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: CheckoutViewModel by viewModels()

    private val cartPreferences: SharedPreferences by lazy {
        getSharedPreferences("cart_preferences", Context.MODE_PRIVATE)
    }

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
        val grandTotal = intent.getDoubleExtra("grandTotal", 0.0)

        if (cartItemsList != null) {
            viewModel.setCartItems(cartItemsList)
        }

        binding.grandPriceText.text = grandTotal.toString()

        loadCartItem()
        observeViewModel()

        // Load shipping address
        loadAddressFromCache()

        // Go To Change Address
        goToChangeAddress()

        // Payment Methods
        paymentBottomSheet()

        // Place Order
        placeOrder(cartItemsList, grandTotal)

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

    private fun loadAddressFromCache() {
        firestore.collection("ShippingAddress").document(uid).collection("addresses")
            .get(Source.CACHE)
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    setNotFoundText()
                } else {
                    val address = documents.documents[0].toObject(Address::class.java)
                    if (address != null) {
                        binding.nameShippingDetails.text = address.name
                        binding.phoneShippingDetails.text = address.phone
                        binding.addressShippingDetails.text = address.address
                        binding.cityShippingDetails.text = address.city
                        binding.zipShippingDetails.text = address.postalCode
                        binding.stateShippingDetails.text = address.state
                        binding.countryShippingDetails.text = address.country
                    } else {
                        setNotFoundText()
                    }
                }
            }
            .addOnFailureListener { e ->
                showSnackbar("Failed to load address: ${e.message}")
                setNotFoundText()
            }
    }

    private fun setNotFoundText() {
        binding.nameShippingDetails.text = "Not Found"
        binding.addressShippingDetails.text = "Not Found"
        binding.cityShippingDetails.text = "Not Found"
        binding.stateShippingDetails.text = "Not Found"
        binding.phoneShippingDetails.text = "Not Found"
        binding.zipShippingDetails.text = "Not Found"
        binding.countryShippingDetails.text = "Not Found"
    }

    private fun goToChangeAddress() {
        binding.changeAddressButton.setOnClickListener {
            val intent = Intent(this, ShippingAddressActivity::class.java)
            startActivity(intent)
        }
    }


    private fun paymentBottomSheet() {
        binding.paymentMethodCard.setOnClickListener {
            val bottomSheet = PaymentMethodBottomSheetFragment()
            bottomSheet.show(supportFragmentManager, "PaymentMethodBottomSheet")
        }
    }

    private fun placeOrder(cartItemsList: List<CartItem>?, grandTotal: Double) {
        binding.placeOrderButton.setOnClickListener {

            if (binding.addressShippingDetails.text == "Not Found") {
                showSnackbar("Please add shipping address first")
            } else {
                // Show the progress dialog
            val progressDialog = ProgressDialogFragment()
            progressDialog.isCancelable = false
            progressDialog.show(supportFragmentManager, ProgressDialogFragment.TAG)

            // Save the receipt/billing info to Firestore
            // Prepare the order data
            val orderData = hashMapOf(
                "uid" to uid,
                "cartItems" to cartItemsList?.map { cartItem ->
                    hashMapOf(
                        "productId" to cartItem.productId,
                        "productName" to cartItem.productName,
                        "productPrice" to cartItem.productPrice,
                        "productImageUrl" to cartItem.productImageUrl,
                        "productCount" to cartItem.productCount,
                        "totalPrice" to cartItem.totalPrice
                    )
                },
                "grandTotal" to grandTotal,
                "date" to FieldValue.serverTimestamp()
            )
            firestore.collection("orders")
                .add(orderData)
                .addOnSuccessListener {
                    // Hide the dialogue
                    progressDialog.dismiss()
                    // Clear the cart
                    clearCart()
                    // Navigate to SuccessActivity
                    startActivity(Intent(this, SuccessActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    // Hide the dialogue
                    progressDialog.dismiss()
                    // Handle the error
                    showSnackbar("Order placement failed: ${e.message}")
                }
            }

        }
    }

    private fun clearCart() {
        val editor = cartPreferences.edit()
        editor.clear()
        editor.apply()
        startActivity(
            Intent(this, SuccessActivity::class.java).putExtra(
                ExtrasRef.SUCCESS_SCREEN,
                ExtrasRef.SUCCESS_MESSAGE
            )
        )
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        // Load shipping address
        loadAddressFromCache()
    }
}
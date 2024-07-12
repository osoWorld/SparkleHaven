package com.example.sparklehaven.product.checkout

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sparklehaven.R
import com.example.sparklehaven.databinding.ActivityShippingAddressBinding
import com.example.sparklehaven.product.checkout.classes.model.Address
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source

class ShippingAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShippingAddressBinding
    private val firestore = FirebaseModule.firebaseFirestore
    private val auth = FirebaseModule.firebaseAuth
    private val currentUser: String = auth.currentUser?.uid ?: ""
    private var currentAddressId: String? = null // Variable to store the current address document ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingAddressBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.progressBar.visibility = View.GONE

        loadAddressFromCache() // Load the address from cache when the activity starts

        binding.saveAddressButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val address = binding.addressEditText.text.toString().trim()
            val city = binding.cityEditText.text.toString().trim()
            val postal = binding.postalEditText.text.toString().trim()
            val state = binding.stateEditText.text.toString().trim()
            val country = binding.countryEditText.text.toString().trim()

            if (validateAddressInput(name, phone, address, city, postal, state, country)) {

                val addressData = Address(
                    name = name,
                    phone = phone,
                    address = address,
                    city = city,
                    postalCode = postal,
                    state = state,
                    country = country
                )

                saveOrUpdateAddress(addressData)
            }
        }
    }

    private fun saveOrUpdateAddress(addressData: Address) {
        binding.progressBar.visibility = View.VISIBLE
        val addressRef = firestore.collection("ShippingAddress").document(currentUser).collection("addresses")

        // Check if we have a current address ID to update
        if (currentAddressId != null) {
            addressRef.document(currentAddressId!!)
                .set(addressData, SetOptions.merge())
                .addOnSuccessListener {
                    showSnackbar("Address updated successfully")
                    fetchAndCacheAddress()  // Fetch and cache the address after saving
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    showSnackbar("Failed to update address: ${e.message}")
                }
        } else {
            addressRef.add(addressData)
                .addOnSuccessListener { documentReference ->
                    currentAddressId = documentReference.id
                    showSnackbar("Address saved successfully")
                    fetchAndCacheAddress()  // Fetch and cache the address after saving
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    showSnackbar("Failed to save address: ${e.message}")
                }
        }
    }

    private fun fetchAndCacheAddress() {
        firestore.collection("ShippingAddress").document(currentUser).collection("addresses")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val address = document.toObject(Address::class.java)
                    // Save address to Firestore cache (handled automatically by Firestore SDK)
                    if (address != null) {
                        currentAddressId = document.id // Store the document ID for updating purposes
                    }
                }
                loadAddressFromCache()  // Load the address from cache after fetching it
                startActivity(Intent(this, CheckoutActivity::class.java))
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                showSnackbar("Failed to fetch address: ${e.message}")
            }
    }

    private fun loadAddressFromCache() {
        firestore.collection("ShippingAddress").document(currentUser).collection("addresses")
            .get(Source.CACHE)
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val address = document.toObject(Address::class.java)
                    if (address != null) {
                        binding.progressBar.visibility = View.GONE
                        binding.nameEditText.setText(address.name)
                        binding.phoneEditText.setText(address.phone)
                        binding.addressEditText.setText(address.address)
                        binding.cityEditText.setText(address.city)
                        binding.postalEditText.setText(address.postalCode)
                        binding.stateEditText.setText(address.state)
                        binding.countryEditText.setText(address.country)
                        currentAddressId = document.id // Store the document ID for updating purposes
                    }
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                showSnackbar("Failed to load address from cache: ${e.message}")
            }
    }

    private fun validateAddressInput(
        name: String,
        phone: String,
        address: String,
        city: String,
        postal: String,
        state: String,
        country: String
    ): Boolean {

        if (name.isEmpty()) {
            binding.nameEditText.error = "Name is required"
            binding.nameEditText.requestFocus()
            return false
        }

        if (phone.isEmpty()) {
            binding.phoneEditText.error = "Phone number is required"
            binding.phoneEditText.requestFocus()
            return false
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            binding.phoneEditText.error = "Enter a valid phone number"
            binding.phoneEditText.requestFocus()
            return false
        }

        if (address.isEmpty()) {
            binding.addressEditText.error = "Address is required"
            binding.addressEditText.requestFocus()
            return false
        }

        if (city.isEmpty()) {
            binding.cityEditText.error = "City is required"
            binding.cityEditText.requestFocus()
            return false
        }

        if (postal.isEmpty()) {
            binding.postalEditText.error = "Postal code is required"
            binding.postalEditText.requestFocus()
            return false
        }

        if (!postal.matches(Regex("^[0-9]{5}(?:-[0-9]{4})?$"))) {
            binding.postalEditText.error = "Enter a valid postal code"
            binding.postalEditText.requestFocus()
            return false
        }

        if (state.isEmpty()) {
            binding.stateEditText.error = "State is required"
            binding.stateEditText.requestFocus()
            return false
        }

        if (country.isEmpty()) {
            binding.countryEditText.error = "Country is required"
            binding.countryEditText.requestFocus()
            return false
        }

        return true
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
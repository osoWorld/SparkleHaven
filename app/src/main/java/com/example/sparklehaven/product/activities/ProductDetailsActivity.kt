package com.example.sparklehaven.product.activities

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
import com.example.sparklehaven.databinding.ActivityProductDetailsBinding
import com.example.sparklehaven.utils.references.ExtrasRef

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var product : Product
    private var productCountText = 1

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

//            setupProductSizeAutoComplete(product.name)
        }
    }

    private fun updateTotalPrice() {
        val price = product.price.toDoubleOrNull() ?: 0.0
        val totalPrice = price * productCountText
        binding.productCount.text = productCountText.toString().padStart(2, '0')
        binding.productTotalPrice.text = totalPrice.toString()
    }

//    private fun setupProductSizeAutoComplete(category: String) {
//        val sizes = when {
//            category.contains("Ring", ignoreCase = true) -> arrayOf("6", "7", "8", "9")
//            category.contains("Necklace", ignoreCase = true) -> arrayOf("16\"", "18\"", "20\"", "22\"")
//            category.contains("Earring", ignoreCase = true) -> arrayOf("Small", "Medium", "Large", "Extra Large")
//            category.contains("Bracelet", ignoreCase = true) -> arrayOf("6.5\"", "7\"", "7.5\"", "8\"")
//            category.contains("Anklet", ignoreCase = true) -> arrayOf("9\"", "9.5\"", "10\"", "10.5\"")
//            else -> emptyArray()
//        }
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sizes)
//        binding.productSizeOutCompleteText.setAdapter(adapter)
//        binding.productSizeOutCompleteText.setOnClickListener {
//            binding.productSizeOutCompleteText.showDropDown()
//        }
//
//        // Add a simple item click listener to log selected item
//        binding.productSizeOutCompleteText.setOnItemClickListener { parent, view, position, id ->
//            val selectedItem = parent.getItemAtPosition(position).toString()
//            Log.d("ViewDetailedProductActivity", "Selected size: $selectedItem")
//        }
//    }
}
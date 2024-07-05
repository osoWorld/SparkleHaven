package com.example.sparklehaven.product.checkout.classes.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.databinding.FragmentPaymentMethodBottomSheetBinding
import com.example.sparklehaven.product.checkout.CreditCardDetailsActivity
import com.example.sparklehaven.product.checkout.classes.adapters.PaymentMethodBottomSheetAdapter
import com.example.sparklehaven.product.checkout.classes.view_model.PaymentMethodBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentMethodBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentPaymentMethodBottomSheetBinding
    private lateinit var paymentMethodBottomSheetAdapter : PaymentMethodBottomSheetAdapter

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: PaymentMethodBottomSheetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentMethodBottomSheetBinding.inflate(layoutInflater)


        // Move to Credit Card Details Screen
        addANewCard()

        // Load and Observe Payment Methods
        loadPaymentMethods()
        observePaymentMethods()

        return binding.root
    }

    private fun addANewCard() {
        binding.addNewCardBtn.setOnClickListener {
            startActivity(Intent(context, CreditCardDetailsActivity::class.java))
        }
    }

    private fun loadPaymentMethods () {
        paymentMethodBottomSheetAdapter = PaymentMethodBottomSheetAdapter()
        binding.paymentMethodsRecView.adapter = paymentMethodBottomSheetAdapter
        binding.paymentMethodsRecView.layoutManager = LinearLayoutManager(context)
    }

    private fun observePaymentMethods () {
        viewModel.paymentMethodList.observe(viewLifecycleOwner) { paymentMethods ->
            paymentMethodBottomSheetAdapter.updateItems(paymentMethods)
        }
    }

}
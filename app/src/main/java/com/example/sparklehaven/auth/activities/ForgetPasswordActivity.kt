package com.example.sparklehaven.auth.activities

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sparklehaven.R
import com.example.sparklehaven.databinding.ActivityForgetPasswordBinding
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding

    private val auth = FirebaseModule.firebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.progressBar.visibility = View.GONE

        binding.resetPasswordButton.setOnClickListener {
            val email = binding.resetEmailEditText.text.toString().trim()
            if (validateEmail(email)) {
                resetPassword(email)
            }
        }
    }

    private fun resetPassword(email: String) {
        binding.progressBar.visibility = View.VISIBLE
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    showSnackbar("Password reset email sent successfully.")
                } else {
                    showSnackbar("Error: ${task.exception?.message}")
                }
            }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) {
            binding.resetEmailEditText.error = "Email is required"
            binding.resetEmailEditText.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.resetEmailEditText.error = "Enter a valid email"
            binding.resetEmailEditText.requestFocus()
            return false
        }

        return true
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
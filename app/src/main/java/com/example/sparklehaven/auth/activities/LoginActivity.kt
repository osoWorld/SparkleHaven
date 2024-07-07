package com.example.sparklehaven.auth.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.DashboardActivity
import com.example.sparklehaven.databinding.ActivityLoginBinding
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseModule.firebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.createAccountBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            loginUser()
        }

        binding.forgetPasswordTextBtn.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (validateInput(email, password)) {
            // Show the progress bar
            binding.progressBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    // Hide the progress bar
                    binding.progressBar.visibility = View.GONE

                    if (task.isSuccessful) {
                        // Login successful, go to DashboardActivity
                        Snackbar.make(binding.root, "Login successful", Snackbar.LENGTH_SHORT).show()
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If login fails, display a message to the user.
                        Snackbar.make(binding.root, "Authentication failed: ${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required"
            binding.emailEditText.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Enter a valid email"
            binding.emailEditText.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required"
            binding.passwordEditText.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.passwordEditText.error = "Password should be at least 6 characters"
            binding.passwordEditText.requestFocus()
            return false
        }

        return true
    }
}
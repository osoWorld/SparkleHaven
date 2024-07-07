package com.example.sparklehaven.auth.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sparklehaven.R
import com.example.sparklehaven.auth.classes.FirebaseUser
import com.example.sparklehaven.dashboard.DashboardActivity
import com.example.sparklehaven.databinding.ActivityRegisterBinding
import com.example.sparklehaven.utils.references.FirebaseRef
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val auth = FirebaseModule.firebaseAuth
    private val firestore = FirebaseModule.firebaseFirestore
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.registerBtn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
        val username = binding.usernameEditText.text.toString().trim()

        if (validateInput(email, password, confirmPassword)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration successful, save user data to Firestore
                        val user = auth.currentUser
                        val uid = user?.uid

                        if (uid != null) {
                            firebaseUser  = FirebaseUser(username, email, password, uid)
                        }

                        firestore.collection(FirebaseRef.USER).document(uid!!).set(firebaseUser).addOnSuccessListener {
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                            .addOnFailureListener { firestoreExp ->
                                Snackbar.make(binding.root, "Error saving user data: ${firestoreExp.message}", Snackbar.LENGTH_SHORT).show()
                            }

                    } else {
                        // If registration fails, display a message to the user.
                        Snackbar.make(binding.root, "Authentication failed: ${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
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

        if (password != confirmPassword) {
            binding.confirmPasswordEditText.error = "Passwords do not match"
            binding.confirmPasswordEditText.requestFocus()
            return false
        }

        if (!binding.termsAndConditionCheckBox.isChecked) {
            Snackbar.make(binding.root, "Please accept the Terms and Conditions", Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
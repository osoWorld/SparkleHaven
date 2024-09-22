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
import com.example.sparklehaven.utils.references.FirebaseRef
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val auth = FirebaseModule.firebaseAuth
    private val firestore = FirebaseModule.firebaseFirestore
    private lateinit var googleApiClient: GoogleApiClient


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

        // Initialize GoogleApiClient
//        initializeGoogleApiClient()

        binding.progressBar.visibility = View.GONE

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

        binding.signInGoogleBtn.setOnClickListener {
            signInWithGoogle()
        }
    }

//    private fun initializeGoogleApiClient() {
//        // Configure Google Sign In
//        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleApiClient = GoogleApiClient.Builder(this)
//            .enableAutoManage(this) { /* Handle GoogleApiClient connection failure */ }
//            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
//            .build()
//    }

    private fun loginUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (validateInput(email, password)) {
            binding.progressBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    binding.progressBar.visibility = View.GONE

                    if (task.isSuccessful) {
                        showSnackbar("Login successful")
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showSnackbar("Authentication failed: ${task.exception?.message}")
                    }
                }
        }
    }

    private fun signInWithGoogle() {
        binding.progressBar.visibility = View.VISIBLE
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Result of the Google sign-in
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            if (result != null) {
                if (result.isSuccess) {
                    val account = result.signInAccount
                    firebaseAuthWithGoogle(account!!)
                } else {
                    binding.progressBar.visibility = View.GONE
                    showSnackbar("Google sign in failed")
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Save User information to Firestore
                        handleGoogleSignInSuccess(user)
                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                    showSnackbar("Authentication failed: ${task.exception?.message}")
                }
            }
    }

    private fun handleGoogleSignInSuccess(user: FirebaseUser) {
        val userData = hashMapOf(
            "username" to user.displayName,
            "email" to user.email,
            "uid" to user.uid,
            "age" to "",
            "userImageUrl" to ""
        )

        firestore.collection(FirebaseRef.USER).document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                showSnackbar("Failed to save user data: ${e.message}")
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

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
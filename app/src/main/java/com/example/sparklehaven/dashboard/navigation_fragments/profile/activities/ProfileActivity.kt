package com.example.sparklehaven.dashboard.navigation_fragments.profile.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.sparklehaven.R
import com.example.sparklehaven.auth.activities.LoginActivity
import com.example.sparklehaven.auth.classes.FirebaseUser
import com.example.sparklehaven.databinding.ActivityProfileBinding
import com.example.sparklehaven.utils.references.FirebaseRef
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Source

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val pickImageRequestCode = 100124
    private var imageUri: Uri? = null

    private val firestore = FirebaseModule.firebaseFirestore
    private val auth = FirebaseModule.firebaseAuth
    private val storage = FirebaseModule.firebaseStorage.reference
    private val userId = auth.currentUser?.uid ?: ""
    private lateinit var googleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize GoogleApiClient
        initializeGoogleApiClient()

        binding.profileProgress.visibility = View.GONE

        // Fetch user data from Firestore cache
        fetchUserData()

        // Set up update profile button listener
        binding.updateProfileButton.setOnClickListener {
            updateUserProfile()
        }

        // Set up user profile image click listener
        binding.userProfileImg.setOnClickListener {
            chooseImageFromGallery()
        }

        binding.uidCopy.setOnClickListener {
            copyToClipboard("UID", binding.profileUid.text.toString())
        }

        binding.emailCopy.setOnClickListener {
            copyToClipboard("Email", binding.profileEmail.text.toString())
        }

        binding.nameEditFocus.setOnClickListener {
            binding.profileName.requestFocus()

            // Move cursor to the end of the text in profileName EditText
            binding.profileName.setSelection(binding.profileName.text.length)
        }

        binding.ageEditFocus.setOnClickListener {
            binding.profileAge.requestFocus()
            binding.profileAge.setSelection(binding.profileAge.text.length)
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }

    }

    private fun initializeGoogleApiClient() {
        // Configure Google Sign In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this) { /* Handle GoogleApiClient connection failure */ }
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()
    }

    private fun copyToClipboard(label: String, text: String) {
        val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)
        showSnackbar("$label copied to clipboard")
    }

    private fun chooseImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequestCode && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.userProfileImg.setImageURI(imageUri)
        }
    }

    private fun fetchUserData() {
        firestore.collection(FirebaseRef.USER).document(userId).get(Source.CACHE)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(FirebaseUser::class.java)
                    user?.let {
                        binding.profileName.setText(it.username)
                        if (it.age != "") {
                            binding.profileAge.setText(it.age)
                        } else {
                            binding.profileAge.hint = "Enter your age"
                            binding.profileAge.setText("")
                        }

                        binding.profileEmail.text = it.email
                        binding.profileUid.text = it.uid

                        if (it.userImageUrl != "") {
                            Glide.with(this).load(it.userImageUrl)
                                .placeholder(R.drawable.splash_img).into(binding.userProfileImg)
                        } else {
                            binding.userProfileImg.setImageResource(R.drawable.splash_img)
                        }
                    }
                } else {
                    // Fetch from server if cache is empty
                    fetchUserDataFromServer()
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                showSnackbar("Error fetching user data: ${exception.message}")
                // Attempt to fetch from server if cache retrieval fails
                fetchUserDataFromServer()
            }
    }

    private fun fetchUserDataFromServer() {
        firestore.collection(FirebaseRef.USER).document(userId).get(Source.SERVER)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(FirebaseUser::class.java)
                    user?.let {
                        binding.profileProgress.visibility = View.GONE
                        binding.profileName.setText(it.username)
                        if (it.age != "") {
                            binding.profileAge.setText(it.age)
                        } else {
                            binding.profileAge.hint = "Enter your age"
                            binding.profileAge.setText("")
                        }
                        binding.profileEmail.text = it.email
                        binding.profileUid.text = it.uid

                        if (it.userImageUrl != "") {
                            Glide.with(this).load(it.userImageUrl)
                                .placeholder(R.drawable.splash_img).into(binding.userProfileImg)
                        } else {
                            binding.userProfileImg.setImageResource(R.drawable.splash_img)
                        }
                    }
                } else {
                    // Handle case where user data does not exist on the server
                    showSnackbar("User data does not exist.")
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                showSnackbar("Error fetching user data from server: ${exception.message}")
            }
    }

    private fun updateUserProfile() {
        val updatedUsername = binding.profileName.text.toString().trim()
        val updatedAge = binding.profileAge.text.toString().trim()

        if (validateInput(updatedUsername, updatedAge)) {
            if (imageUri != null) {
                uploadImageAndSaveUserProfile(updatedUsername, updatedAge)
            } else {
                saveUserProfile(updatedUsername, updatedAge, null)
            }
        }
    }

    private fun uploadImageAndSaveUserProfile(username: String, age: String) {
        binding.profileProgress.visibility = View.VISIBLE
        val ref = storage.child("profile_images/$userId.jpg")
        val uploadTask = ref.putFile(imageUri!!)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                saveUserProfile(username, age, downloadUri.toString())
            } else {
                binding.profileProgress.visibility = View.GONE
                showSnackbar("Image upload failed: ${task.exception?.message}")
            }
        }
    }

    private fun saveUserProfile(username: String, age: String, imageUrl: String?) {
        val updatedUser = mutableMapOf<String, Any>(
            "username" to username,
            "age" to age
        )

        imageUrl?.let {
            updatedUser["userImageUrl"] = it
        }

        firestore.collection(FirebaseRef.USER).document(userId).update(updatedUser)
            .addOnSuccessListener {

                // Fetch updated data and update cache and UI
                fetchUserDataFromServer()
                showSnackbar("Profile updated successfully.")
            }
            .addOnFailureListener { exception ->
                binding.profileProgress.visibility = View.GONE
                showSnackbar("Error updating profile: ${exception.message}")
            }
    }

    private fun validateInput(username: String, age: String): Boolean {
        if (username.isEmpty()) {
            binding.profileName.error = "Name is required"
            binding.profileName.requestFocus()
            return false
        }

        if (age.isEmpty()) {
            binding.profileAge.error = "Age is required"
            binding.profileAge.requestFocus()
            return false
        }

        if (age.toInt() > 200) {
            binding.profileAge.error = "Enter valid Age"
            binding.profileAge.requestFocus()
            return false
        }

        return true
    }

    private fun logout() {
        // Connect GoogleApiClient before attempting to sign out
        googleApiClient.connect()
        googleApiClient.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(bundle: Bundle?) {
                // Sign out the user from Firebase Authentication
                auth.signOut()

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback {
                    // User is now signed out
                    googleApiClient.disconnect()
                    redirectToLogin()
                }

                revokeAccess()
            }

            override fun onConnectionSuspended(i: Int) {

            }
        })
    }

    private fun revokeAccess() {
        // Connect GoogleApiClient before attempting to revoke access
        googleApiClient.connect()
        googleApiClient.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(bundle: Bundle?) {
                auth.signOut()
                Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback {
                    // User access is revoked
                    googleApiClient.disconnect()
                    redirectToLogin()
                }
            }

            override fun onConnectionSuspended(i: Int) {

            }
        })
    }

    private fun redirectToLogin() {
        // Redirect the user to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
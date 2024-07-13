package com.example.sparklehaven.dashboard.navigation_fragments.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sparklehaven.R
import com.example.sparklehaven.auth.classes.FirebaseUser
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters.SettingsItemAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.view_model.SettingsViewModel
import com.example.sparklehaven.databinding.FragmentProfileBinding
import com.example.sparklehaven.utils.references.FirebaseRef
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Source

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var upperSettingsAdapter: SettingsItemAdapter
    private lateinit var lowerSettingsAdapter: SettingsItemAdapter
    private val firestore = FirebaseModule.firebaseFirestore
    private val userId = FirebaseModule.firebaseAuth.currentUser?.uid ?: ""

    // Initialize ViewModel using viewModels() delegate
    private val viewModel : SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

//        // Fetch user data from Firestore
//        fetchUserData()

        setupRecyclerViews()
        observeViewModel()

        return binding.root
    }

    private fun fetchUserData() {
        firestore.collection(FirebaseRef.USER).document(userId).get(Source.CACHE)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(FirebaseUser::class.java)
                    user?.let {
                        binding.usernameText.text = it.username
                        binding.userEmailText.text = it.email

                        if (user.userImageUrl != ""){
                            Glide.with(this)
                                .load(it.userImageUrl)
                                .placeholder(R.drawable.splash_img)
                                .into(binding.userImg)
                        } else {
                            binding.userImg.setImageResource(R.drawable.splash_img)
                        }

                    }
                } else {
                    // Fetch from server if cache is empty
//                    fetchUserDataFromServer()
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                showSnackbar("Error fetching user data: ${exception.message}")
                // Attempt to fetch from server if cache retrieval fails
//                fetchUserDataFromServer()
            }
    }

    private fun setupRecyclerViews() {
        upperSettingsAdapter = SettingsItemAdapter(requireContext(), childFragmentManager)
        lowerSettingsAdapter = SettingsItemAdapter(requireContext(),childFragmentManager)

        binding.upperSettingItemRecView.layoutManager = LinearLayoutManager(context)
        binding.upperSettingItemRecView.adapter = upperSettingsAdapter

        binding.lowerSettingItemsRecView.layoutManager = LinearLayoutManager(context)
        binding.lowerSettingItemsRecView.adapter = lowerSettingsAdapter
    }

    private fun observeViewModel() {
        viewModel.upperSettingListItems.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                upperSettingsAdapter.updateItems(items)
            }
        }

        viewModel.lowerSettingListItems.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                lowerSettingsAdapter.updateItems(items)
            }
        }
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
    override fun onResume() {
        super.onResume()
        // Fetch user data from Firestore
        fetchUserData()

        viewModel.loadUpperSettingsItems()
        viewModel.loadLowerSettingsItems()
    }
}
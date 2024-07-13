package com.example.sparklehaven.dashboard.navigation_fragments.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.sparklehaven.R
import com.example.sparklehaven.auth.classes.FirebaseUser
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.classes.view_model.FavoriteViewModel
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.viewmodel_factory.FavoriteViewModelFactory
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.adapters.HomeCategoryAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.adapters.ImageSliderAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.adapters.ProductItemsAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.home.classes.view_models.HomeViewModel
import com.example.sparklehaven.data.local.AppDatabase
import com.example.sparklehaven.data.repository.FavoriteRepository
import com.example.sparklehaven.databinding.FragmentHomeBinding
import com.example.sparklehaven.product.add_to_cart.AddToCartActivity
import com.example.sparklehaven.utils.references.FirebaseRef
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Source
import kotlin.math.abs

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ImageSliderAdapter
    private lateinit var categoryAdapter: HomeCategoryAdapter
    private lateinit var productItemAdapter: ProductItemsAdapter
    private val userId = FirebaseModule.firebaseAuth.currentUser?.uid ?: ""

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: HomeViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(
            userId,
            FavoriteRepository(
                AppDatabase.getDatabase(requireContext()).favoriteDao(),
                FirebaseModule.firebaseFirestore
            )
        )
    }

    private val runnable = Runnable {
        binding.viewPagerImageSlider.currentItem += 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        // Fetch user data from Firestore
        fetchUserData()

        // ImageSlider
        initImageSlider()
        setUpTransformer()

        // ImageSlider Indicators
        setUpIndicators()
        setCurrentIndicator(0)

        // Categories RecyclerView
        categories()

        // Items According to categories RecyclerView
        productItems()

        // Go To cart
        goToCart()

        binding.viewPagerImageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Update the indicator using modulo operation to handle infinite loop correctly
                setCurrentIndicator(position % imageList.size)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })

        return binding.root
    }

    private fun fetchUserData() {
        val firestore = FirebaseModule.firebaseFirestore
        firestore.collection(FirebaseRef.USER).document(userId).get(Source.CACHE)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(FirebaseUser::class.java)
                    user?.let {
                        binding.usernameText.text = it.username

                        if (user.userImageUrl != ""){
                            Glide.with(this)
                                .load(it.userImageUrl)
                                .placeholder(R.drawable.splash_img)
                                .into(binding.userHomeImg)
                        } else {
                            binding.userHomeImg.setImageResource(R.drawable.splash_img)
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
        val firestore = FirebaseModule.firebaseFirestore
        firestore.collection(FirebaseRef.USER).document(userId).get(Source.SERVER)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(FirebaseUser::class.java)
                    user?.let {
                        binding.usernameText.text = it.username

                        if (user.userImageUrl != "") {
                            Glide.with(this)
                                .load(it.userImageUrl)
                                .placeholder(R.drawable.splash_img)
                                .into(binding.userHomeImg)
                        } else {
                            binding.userHomeImg.setImageResource(R.drawable.splash_img)
                        }

                    }
                } else {
                    // Handle case where user data does not exist on the server
                    showSnackbar("User data does not exist")
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                showSnackbar("Error fetching user data from server: ${exception.message}")
            }
    }


    private fun initImageSlider() {
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()
        imageList.add(R.drawable.black_and_white_silver_earrings)
        imageList.add(R.drawable.black_diamond_ring_limited_tag)
        imageList.add(R.drawable.blue_gray_modern_rings)
        imageList.add(R.drawable.golden_wedding_rings)

        adapter = ImageSliderAdapter(imageList, binding.viewPagerImageSlider)
        binding.viewPagerImageSlider.adapter = adapter

        binding.viewPagerImageSlider.apply {
            offscreenPageLimit = 2
            clipToPadding = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        // Set initial item to a multiple of imageList.size to handle looping
        binding.viewPagerImageSlider.setCurrentItem(imageList.size * 1000, false)
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.viewPagerImageSlider.setPageTransformer(transformer)
    }

    private fun setUpIndicators() {
        val indicators = arrayOfNulls<ImageView>(imageList.size)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(context)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.indicator_inactive
                )
            )
            indicators[i]?.layoutParams = layoutParams
            binding.indicatorsLayout.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorsLayout.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorsLayout.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }

    private fun refreshIndicator() {
        // Refreshing the indicator on resume using modulo operation
        val currentItem = binding.viewPagerImageSlider.currentItem % imageList.size
        setCurrentIndicator(currentItem)
    }

    private fun categories () {
        categoryAdapter = HomeCategoryAdapter(ArrayList()) { category ->
            viewModel.filterProductsByCategories(category)
        }
        binding.categoryHomeRecView.adapter = categoryAdapter

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateCategories(categories)
        }
    }

    private fun productItems () {
        productItemAdapter = ProductItemsAdapter(ArrayList(), requireContext(), favoriteViewModel)
        binding.productItemsRecView.adapter = productItemAdapter
        binding.productItemsRecView.layoutManager = GridLayoutManager(context,2)

        viewModel.products.observe(viewLifecycleOwner) { products ->
            productItemAdapter.updateItems(products)
        }

        // Fetch and observe favorites
       favoriteViewModel.fetchFavorites()
    }

    private fun goToCart () {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(context, AddToCartActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 2000)
        refreshIndicator() // Refreshing the indicator on resume
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
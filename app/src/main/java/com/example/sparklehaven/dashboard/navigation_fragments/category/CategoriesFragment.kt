package com.example.sparklehaven.dashboard.navigation_fragments.category

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.dashboard.navigation_fragments.category.classes.adapters.CategoryAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.category.classes.view_model.CategoryViewModel
import com.example.sparklehaven.databinding.FragmentCategoriesBinding
import com.example.sparklehaven.product.activities.SearchProductActivity

class CategoriesFragment : Fragment() {
    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var favoriteItemAdapter: CategoryAdapter

    // Initialize ViewModel using viewModels() delegate
    private val viewModel: CategoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)

        binding.searchView.apply {
            isFocusable = false
            isFocusableInTouchMode = false
            isClickable = true
            setOnClickListener {
                goToSearchProduct()
            }
        }

        // Load & observe CategoryItems
        categoryItems()
        observeCategoryItems()

        return binding.root
    }

    private fun categoryItems () {
        favoriteItemAdapter = CategoryAdapter(requireContext())
        binding.categoryRecView.adapter = favoriteItemAdapter
        binding.categoryRecView.layoutManager = LinearLayoutManager(context)

    }

    private fun goToSearchProduct () {
        startActivity(Intent(context, SearchProductActivity::class.java))
    }

    private fun observeCategoryItems () {
        viewModel.categoryList.observe(viewLifecycleOwner) { categoryItems ->
            favoriteItemAdapter.updateItems(categoryItems)
        }
    }
}
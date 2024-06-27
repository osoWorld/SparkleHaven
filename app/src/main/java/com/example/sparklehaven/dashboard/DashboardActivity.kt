package com.example.sparklehaven.dashboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.category.CategoriesFragment
import com.example.sparklehaven.dashboard.navigation_fragments.favorite.FavoriteFragment
import com.example.sparklehaven.dashboard.navigation_fragments.home.HomeFragment
import com.example.sparklehaven.dashboard.navigation_fragments.profile.ProfileFragment
import com.example.sparklehaven.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        replaceFragment(HomeFragment())

        binding.bubbleTabBar.addBubbleListener { id ->
            when (id) {
                R.id.home_nav -> {
                    replaceFragment(HomeFragment())
                }

                R.id.category_nav -> {
                    replaceFragment(CategoriesFragment())
                }

                R.id.favorite_nav -> {
                    replaceFragment(FavoriteFragment())
                }

                R.id.profile_nav -> {
                    replaceFragment(ProfileFragment())
                }
            }

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameContainer, fragment)
        fragmentTransaction.commit()
    }
}
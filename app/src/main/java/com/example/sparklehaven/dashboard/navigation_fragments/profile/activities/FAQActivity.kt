package com.example.sparklehaven.dashboard.navigation_fragments.profile.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters.FAQAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.FAQItem
import com.example.sparklehaven.databinding.ActivityFaqactivityBinding

class FAQActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaqactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqactivityBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val faqList = listOf(
            FAQItem("What is SparkleHaven?", "SparkleHaven is an app that allows you to explore and purchase a variety of jewelry."),
            FAQItem("How do I create an account?", "To create an account, click on the Register button on the home screen and fill in the required details."),
            FAQItem("How do I make a purchase?", "You can browse through the products, add them to your cart, and proceed to checkout to make a purchase."),
            FAQItem("How do I contact support?", "You can contact support through the Help section in the app."),
            FAQItem("How do I update my profile?", "You can update your profile by going to Profile screen and from there tap on the Profile option, to visit and update your profile.")
        )

        binding.recyclerViewFAQ.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFAQ.adapter = FAQAdapter(faqList)

    }
}
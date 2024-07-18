package com.example.sparklehaven.dashboard.navigation_fragments.profile.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters.PrivacyPolicyAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.FAQItem
import com.example.sparklehaven.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding
    private lateinit var privacyPolicyAdapter: PrivacyPolicyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadPrivacyPolicy()

    }

    private fun loadPrivacyPolicy() {
        val answer1: String = getString(R.string.privacy_policy_answer1)
        val answer2: String = getString(R.string.privacy_policy_answer2)
        val answer3: String = getString(R.string.privacy_policy_answer3)
        val answer4: String = getString(R.string.privacy_policy_answer4)
        val answer5: String = getString(R.string.privacy_policy_answer5)

        val privacyList = listOf(
            FAQItem("What information do we collect about you?", answer1),
            FAQItem("How do we use your information?", answer2),
            FAQItem("To whom do we disclose your information?", answer3),
            FAQItem("How do we keep your information secure?", answer4),
            FAQItem("How can you manage your information?", answer5),
        )

        privacyPolicyAdapter = PrivacyPolicyAdapter(privacyList)
        binding.recyclerViewFAQ.adapter = privacyPolicyAdapter
        binding.recyclerViewFAQ.layoutManager = LinearLayoutManager(this)
    }
}
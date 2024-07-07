package com.example.sparklehaven.splash

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sparklehaven.R
import com.example.sparklehaven.auth.activities.LoginActivity
import com.example.sparklehaven.data.local.AppDatabase
import com.example.sparklehaven.databinding.ActivitySplashBinding
import com.example.sparklehaven.utils.singleton.NetworkModule
import com.example.sparklehaven.utils.singleton.RoomModule
import com.example.sparklehaven.utils.singleton.SharedPreferencesModule
import com.google.firebase.FirebaseApp

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val MAX_LEVEL: Int = 10000
    private val REVEAL_DURATION: Long = 3350 // Animation duration in milliseconds
    private var hasAnimationPlayed: Boolean = false
    private lateinit var clipDrawable: ClipDrawable
    private lateinit var animator: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // FirebaseApp.initializeApp(this) // Initialize Firebase first
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase, Room, SharedPreferences, and NetworkModule
//        instances()

        setupAnimation()
    }

    override fun onResume() {
        super.onResume()
        if (!hasAnimationPlayed) {
            startAnimation()
        } else {
            goToNextActivity()
        }
    }

    private fun setupAnimation() {
        clipDrawable = ClipDrawable(
            resources.getDrawable(R.drawable.sparkle_haven_redesign_f_f),
            ClipDrawable.VERTICAL,
            ClipDrawable.HORIZONTAL
        )
        binding.appLogo.setImageDrawable(clipDrawable)

        animator = ValueAnimator.ofInt(0, MAX_LEVEL).apply {
            duration = REVEAL_DURATION
            addUpdateListener { animation: ValueAnimator ->
                val level = animation.animatedValue as Int
                clipDrawable.level = level
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    hasAnimationPlayed = true
                    goToNextActivity()
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
    }

    private fun startAnimation() {
        animator.start()
    }

    private fun goToNextActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

//    private fun instances() {
//        // Using FirebaseModule
//        val auth = FirebaseModule.firebaseAuth
//        val firestore = FirebaseModule.firebaseFirestore
//        val storage = FirebaseModule.firebaseStorage
//
//        // Using SharedPreferencesModule
//        val sharedPreferences = SharedPreferencesModule.getSharedPreferences(this)
//
//        // Using NetworkModule
//        val retrofit = NetworkModule.retrofit
//
//        // Using RoomModule
//        val database = AppDatabase.getDatabase(this)
//        val userDao = database.userDao()
//    }
}
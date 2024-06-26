package com.example.sparklehaven.splash

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sparklehaven.R
import com.example.sparklehaven.auth.activities.LoginActivity
import com.example.sparklehaven.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val MAX_LEVEL: Int = 10000
    private val REVEAL_DURATION: Long = 3700 // Animation duration in milliseconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Create a ClipDrawable to control the reveal animation
        val clipDrawable = ClipDrawable(resources.getDrawable(R.drawable.sparkle_haven_redesign_f_f),
            ClipDrawable.VERTICAL,
            ClipDrawable.HORIZONTAL
        )
        binding.appLogo.setImageDrawable(clipDrawable)


        // Create a ValueAnimator for the reveal animation
        val animator = ValueAnimator.ofInt(0, MAX_LEVEL)
        animator.setDuration(REVEAL_DURATION)
        animator.addUpdateListener { animation: ValueAnimator ->
            val level = animation.animatedValue as Int
            clipDrawable.setLevel(level)
        }


        // Start the animation
        animator.start()

        // Timer to go to new activity
        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, REVEAL_DURATION + 100)
    }
}
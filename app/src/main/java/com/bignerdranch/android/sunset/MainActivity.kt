package com.bignerdranch.android.sunset

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bignerdranch.android.sunset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // View binding for the layout
    private lateinit var binding: ActivityMainBinding

    // Colors for the sky at different times of the animation
    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a click listener on the scene to start the animation
        binding.scene.setOnClickListener {
            startAnimation()
        }
    }

    private fun startAnimation() {
        // Initial and final positions for the sun's vertical movement
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()

        // shark fin code //
        // Initial and final positions for the shark fin's horizontal movement
        val finXStart = binding.ocean.left.toFloat() - 260
        val finXEnd = binding.ocean.width.toFloat()

        // Create an ObjectAnimator for the sun's vertical movement
        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        // Create an ObjectAnimator for the background color transition to simulate sunset
        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        // Create an ObjectAnimator for the background color transition to simulate night
        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        // shark fin code //
        // Create an ObjectAnimator for the shark fin's horizontal movement
        val finAnimator = ObjectAnimator
            .ofFloat(binding.fin, "x", finXStart, finXEnd)
            .setDuration(1500)
        finAnimator.interpolator = AccelerateInterpolator()

        // Create an AnimatorSet to coordinate multiple animations
        val animatorSet = AnimatorSet()
        animatorSet.doOnStart { binding.fin.isVisible = true } // Show shark fin at the start of the animation
        animatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)
            .before(finAnimator)
        animatorSet.start()
    }
}

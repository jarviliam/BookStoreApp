package com.example.googlebooksapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.googlebooksapi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /*
    fun hideNavigation(animate: Boolean = true) {
        bottomNav.run {
            isEnabled = false
            isClickable = false
        }
        bottomNav.animate()
            .translationY(resources.getDimensionPixelSize(R.dimen.navHeight).toFloat())
            .setDuration(350L).setInterpolator(DecelerateInterpolator(2F)).start()
    }

    fun showNavigation(animate: Boolean = true) {
        bottomNav.run {
            isEnabled = true
            isClickable = true
        }
        bottomNav.animate()
            .translationY(resources.getDimensionPixelSize(R.dimen.navHeight).toFloat())
            .setDuration(350L).setInterpolator(
                DecelerateInterpolator(2F)
            ).start()
    }*/
}
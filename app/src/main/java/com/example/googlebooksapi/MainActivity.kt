package com.example.googlebooksapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import com.example.googlebooksapi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.i("Main Act")
        with(binding) {

        }
        //Navigation.findNavController(this,R.id.nav_host).navigate()
    }

    fun hideNavigation(animate: Boolean = true) {
        bottomNav.run {
            isEnabled = false
            isClickable = false
        }
        bottomNav.animate()
            .translationY(resources.getDimensionPixelSize(R.dimen.navHeight).toFloat())
            .setDuration(350L).setInterpolator(DecelerateInterpolator(2F)).start()
    }
}
package com.koose.ispacerecipeapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.koose.ispacerecipeapp.MainActivity
import com.koose.ispacerecipeapp.R
import com.koose.ispacerecipeapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    //institte view binding
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition { true }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
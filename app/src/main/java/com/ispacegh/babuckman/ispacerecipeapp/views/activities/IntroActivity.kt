package com.ispacegh.babuckman.ispacerecipeapp.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ispacegh.babuckman.ispacerecipeapp.MainActivity
import com.ispacegh.babuckman.ispacerecipeapp.R
import com.ispacegh.babuckman.ispacerecipeapp.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding:ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        binding = ActivityIntroBinding.inflate(layoutInflater)

        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition {
            true
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        setContentView(binding.root)
    }
}
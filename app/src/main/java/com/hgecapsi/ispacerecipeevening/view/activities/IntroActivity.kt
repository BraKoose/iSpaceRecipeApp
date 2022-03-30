package com.hgecapsi.ispacerecipeevening.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hgecapsi.ispacerecipeevening.MainActivity
import com.hgecapsi.ispacerecipeevening.R
import com.hgecapsi.ispacerecipeevening.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }
        startActivity(Intent(this,MainActivity::class.java))
        finish()
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
package com.ispacegh.babuckman.ispacerecipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ispacegh.babuckman.ispacerecipeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // TODO Step 4: Make the navController variable as global variable.
    // START
    private lateinit var mNavController: NavController
    // END

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mNavController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.allRecipeFragment, R.id.favoriteFragment, R.id.randomDishFragment
        ))
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(mNavController)




    }
}
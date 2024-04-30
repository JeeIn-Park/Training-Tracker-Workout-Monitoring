package com.jeein.trainingtracker

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jeein.trainingtracker.databinding.ActivityMainBinding
import com.jeein.trainingtracker.ui.muscles.MuscleFactory
import com.jeein.trainingtracker.ui.tag.TagFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_status, R.id.navigation_setting))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> navView.menu.findItem(R.id.navigation_home).isChecked = true
                R.id.navigation_add_card -> navView.menu.findItem(R.id.navigation_home).isChecked = true
                R.id.navigation_add_log -> navView.menu.findItem(R.id.navigation_home).isChecked = true
                R.id.navigation_status -> navView.menu.findItem(R.id.navigation_status).isChecked = true
                R.id.navigation_setting -> navView.menu.findItem(R.id.navigation_setting).isChecked = true
                R.id.navigation_privacy_policy -> navView.menu.findItem(R.id.navigation_setting).isChecked = true
            }
        }

        MuscleFactory.refreshMuscle(this)
    }

    override fun onStop() {
        super.onStop()
        TagFactory.resetSelection(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

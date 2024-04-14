package com.jeein.trainingtracker

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.databinding.ActivityMainBinding
import com.jeein.trainingtracker.ui.muscles.MuscleFactory
import com.jeein.trainingtracker.ui.tag.TagFactory

class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding

//private lateinit var statusCardAdapter: StatusCardAdapter
//private lateinit var homeCardAdapter: HomeCardAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_status, R.id.navigation_setting))

        //TODO : deal with data sync
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        MuscleFactory.refreshMuscle(this)
    }

    override fun onStop() {
        super.onStop()
        TagFactory.resetSelection(this)
    }
}
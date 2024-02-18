package com.example.smiley.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smiley.R
import com.example.smiley.databinding.ActivityDentistDashboardBinding

class DentistDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDentistDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDentistDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavView.setupWithNavController(navController)
    }
}
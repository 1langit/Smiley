package com.example.smiley.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.databinding.ActivityWelcomeBinding
import com.example.smiley.utils.PrefManager

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this@WelcomeActivity)
        if (prefManager.isLoggedIn()) {
            startActivity(
                Intent(
                    this@WelcomeActivity,
                    if (prefManager.getRole() == "patient") {
                        PatientDashboardActivity::class.java
                    } else {
                        DentistDashboardActivity::class.java
                    }
                )
            )
            finish()
        }

        with(binding){
            btnPatient.setOnClickListener {
                val newIntent = Intent(this@WelcomeActivity, OnBoardingActivity::class.java)
                newIntent.putExtra("role", "patient")
                startActivity(newIntent)
            }

            btnDentist.setOnClickListener {
                val newIntent = Intent(this@WelcomeActivity, OnBoardingActivity::class.java)
                newIntent.putExtra("role", "dentist")
                startActivity(newIntent)
            }
        }
    }
}
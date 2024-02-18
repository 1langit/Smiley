package com.example.smiley.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnLogin.setOnClickListener {
                val newIntent = Intent(this@OnBoardingActivity, LoginActivity::class.java)
                newIntent.putExtra("role", intent.getStringExtra("role"))
                startActivity(newIntent)
            }

            btnSignup.setOnClickListener {
                val newIntent = Intent(this@OnBoardingActivity, RegisterActivity::class.java)
                newIntent.putExtra("role", intent.getStringExtra("role"))
                startActivity(newIntent)
            }
        }
    }
}
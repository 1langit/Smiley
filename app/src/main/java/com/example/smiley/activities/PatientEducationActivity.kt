package com.example.smiley.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smiley.R
import com.example.smiley.databinding.ActivityPatientEducationBinding

class PatientEducationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientEducationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientEducationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}
package com.example.smiley.activities

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.smiley.R
import com.example.smiley.databinding.ActivityPatientClassificationBinding

class PatientClassificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientClassificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientClassificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            imgTeeth.setImageBitmap(BitmapFactory.decodeFile(intent.getStringExtra("image")))
            txtResult.text = intent.getStringExtra("result")
            txtDescription.text = intent.getStringExtra("description")

            btnConsult.setOnClickListener {
                val newIntent = Intent(this@PatientClassificationActivity, PatientDashboardActivity::class.java)
                newIntent.putExtra("session", "dentist")
                startActivity(newIntent)
                finish()
            }

            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}
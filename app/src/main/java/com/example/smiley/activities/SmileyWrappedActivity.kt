package com.example.smiley.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smiley.R
import com.example.smiley.databinding.ActivitySmileyWrappedBinding

class SmileyWrappedActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySmileyWrappedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmileyWrappedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package com.example.smiley.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.databinding.ActivityProfileEditBinding
import com.example.smiley.utils.PrefManager

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this@ProfileEditActivity)
        when(prefManager.getRole()) {
            "patient" -> patientForm()
            else -> dentistForm()
        }

        with(binding) {
            btnBack.setOnClickListener {
                finish()
            }

            btnSave.setOnClickListener {
                finish()
            }
        }
    }

    private fun patientForm() {
        with(binding) {
            txtCity.visibility = View.GONE
            boxCity.visibility = View.GONE
        }
    }

    private fun dentistForm() {
        with(binding) {
            txtSex.visibility = View.GONE
            boxSex.visibility = View.GONE
            txtAge.visibility = View.GONE
            boxAge.visibility = View.GONE
        }
    }
}
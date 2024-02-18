package com.example.smiley.activities

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.R
import com.example.smiley.databinding.ActivityPatientJournalBinding

class PatientJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientJournalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showInfoDialog()

        with(binding) {
            btnBack.setOnClickListener {
                finish()
            }

            btnSave.setOnClickListener {
                Toast.makeText(this@PatientJournalActivity, "Journal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showInfoDialog() {
        val dialog = AlertDialog.Builder(this@PatientJournalActivity)
            .setTitle("Welcome to the journal!")
            .setMessage(getString(R.string.journal_info))
            .setPositiveButton("Got it!") { dialog, _ ->
                dialog.dismiss()
            }.create()
        dialog.show()
    }
}
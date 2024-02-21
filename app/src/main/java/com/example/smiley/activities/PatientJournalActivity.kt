package com.example.smiley.activities

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.R
import com.example.smiley.databinding.ActivityPatientJournalBinding
import com.example.smiley.models.Journal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PatientJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientJournalBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        with(binding) {
            btnBack.setOnClickListener {
                finish()
            }

            btnSave.setOnClickListener {
                val journal = Journal(
                    uid = firebaseAuth.currentUser?.uid!!,
                    date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM yyyy")),
                    brushTeeth = check1.isChecked,
                    noDentalProblem = check2.isChecked,
                    lessSugar = check3.isChecked,
                    checkup = check4.isChecked
                )

                firestore.collection("journal")
                    .add(journal)
                    .addOnFailureListener {
                        Toast.makeText(this@PatientJournalActivity, it.message, Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener { document ->
                        journal.id = document.id
                        document.update("id", journal.id).addOnFailureListener {
                            Toast.makeText(this@PatientJournalActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                Toast.makeText(this@PatientJournalActivity, "Journal saved", Toast.LENGTH_SHORT).show()
                finish()
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
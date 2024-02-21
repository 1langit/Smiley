package com.example.smiley.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.databinding.ActivityProfileEditBinding
import com.example.smiley.models.Dentist
import com.example.smiley.models.Patient
import com.example.smiley.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var prefManager: PrefManager
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
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

            FirebaseFirestore.getInstance()
                .collection("patient")
                .document(firebaseAuth.currentUser?.uid!!)
                .get()
                .addOnFailureListener {
                    Toast.makeText(this@ProfileEditActivity, it.message, Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    if (!it.exists()) {
                        Toast.makeText(this@ProfileEditActivity, "User not found", Toast.LENGTH_SHORT).show()
                    } else {
                        val user = it.toObject(Patient::class.java)
                        edtName.setText(user?.name)
                        edtSex.setText(user?.sex)
                        edtAge.setText(user?.age.toString())
                        edtEmail.setText(user?.email)
                    }
                }
        }
    }

    private fun dentistForm() {
        with(binding) {
            txtSex.visibility = View.GONE
            boxSex.visibility = View.GONE
            txtAge.visibility = View.GONE
            boxAge.visibility = View.GONE

            FirebaseFirestore.getInstance()
                .collection("dentist")
                .document(firebaseAuth.currentUser?.uid!!)
                .get()
                .addOnFailureListener {
                    Toast.makeText(this@ProfileEditActivity, it.message, Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    if (!it.exists()) {
                        Toast.makeText(this@ProfileEditActivity, "User not found", Toast.LENGTH_SHORT).show()
                    } else {
                        val user = it.toObject(Dentist::class.java)
                        edtName.setText(user?.name)
                        edtCity.setText(user?.city)
                        edtEmail.setText(user?.email)
                    }
                }
        }
    }
}
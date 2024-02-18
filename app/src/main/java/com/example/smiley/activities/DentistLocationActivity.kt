package com.example.smiley.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smiley.R
import com.example.smiley.databinding.ActivityDentistLocationBinding
import com.example.smiley.models.Dentist
import com.example.smiley.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DentistLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDentistLocationBinding
    private lateinit var prefManager: PrefManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDentistLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this@DentistLocationActivity)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        with(binding) {
            btnRegister.setOnClickListener {
                val name = intent.getStringExtra("name")
                val city = intent.getStringExtra("city")
                val email = intent.getStringExtra("email")
                val address = edtAddress.text.toString()
                val buildingNumber = edtBuildingNum.text.toString()

                if (address.isBlank()) {
                    Toast.makeText(this@DentistLocationActivity, "Please fill out the address", Toast.LENGTH_SHORT).show()
                } else {
                    val uid = firebaseAuth.currentUser?.uid!!
                    firestore.collection("dentist")
                        .document(uid)
                        .set(Dentist(uid, name!!, city!!, address, buildingNumber, email!!))
                        .addOnFailureListener {
                            Toast.makeText(this@DentistLocationActivity, it.message, Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener {
                            Toast.makeText(this@DentistLocationActivity, "Register success", Toast.LENGTH_SHORT).show()
                            prefManager.setLoggedIn(true)
                            prefManager.saveRole("dentist")

                            val newIntent = Intent(this@DentistLocationActivity, DentistDashboardActivity::class.java)
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(newIntent)
                        }
                }
            }
        }
    }
}
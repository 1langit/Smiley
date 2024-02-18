package com.example.smiley.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.databinding.ActivityRegisterBinding
import com.example.smiley.models.Patient
import com.example.smiley.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this@RegisterActivity)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        when(intent.getStringExtra("role")) {
            "patient" -> patientForm()
            else -> dentistForm()
        }
    }

    private fun patientForm() {
        val patientCollection = firestore.collection("patient")

        with(binding) {
            txtCity.visibility = View.GONE
            boxCity.visibility = View.GONE

            btnRegister.setOnClickListener {
                val name = edtName.text.toString()
                val sex = edtSex.text.toString()
                val age = edtAge.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                val confirmPassword = edtConfirmPassword.text.toString()
                val inputs = listOf(name, sex, age, email, password)

                if (inputs.any { it.isBlank() }) {
                    Toast.makeText(this@RegisterActivity, "Please fill in all required info", Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnFailureListener {
                        Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener {
                        val uid = firebaseAuth.currentUser?.uid!!
                        patientCollection.document(uid)
                            .set(Patient(uid, name, sex, age.toInt(), email))
                            .addOnFailureListener {
                                Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                            }.addOnSuccessListener {
                                Toast.makeText(this@RegisterActivity, "Register success", Toast.LENGTH_SHORT).show()
                                prefManager.setLoggedIn(true)
                                prefManager.saveRole("patient")

                                val newIntent = Intent(this@RegisterActivity, PatientDashboardActivity::class.java)
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(newIntent)
                            }
                    }
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
            btnRegister.text = "Next"

            btnRegister.setOnClickListener {
                val name = edtName.text.toString()
                val city = edtCity.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                val confirmPassword = edtConfirmPassword.text.toString()
                val inputs = listOf(name, city, email, password)

                if (inputs.any { it.isBlank() }) {
                    Toast.makeText(this@RegisterActivity, "Please fill in all required info", Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnFailureListener {
                        Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener {
                        val newIntent = Intent(this@RegisterActivity, DentistLocationActivity::class.java)
                        newIntent.putExtra("name", name)
                        newIntent.putExtra("city", city)
                        newIntent.putExtra("email", email)
                        startActivity(newIntent)
                    }
                }

            }
        }
    }
}
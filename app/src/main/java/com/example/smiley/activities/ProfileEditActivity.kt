package com.example.smiley.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.R
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
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        prefManager = PrefManager.getInstance(this@ProfileEditActivity)
        when(prefManager.getRole()) {
            "patient" -> patientForm()
            else -> dentistForm()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun patientForm() {
        with(binding) {
            txtCity.visibility = View.GONE
            boxCity.visibility = View.GONE
            txtClinicName.visibility = View.GONE
            boxClinicName.visibility = View.GONE
            txtClinicAddress.visibility = View.GONE
            boxClinicAddress.visibility = View.GONE

            firestore.collection("patient")
                .document(firebaseAuth.currentUser!!.uid)
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
                        edtSex.setSimpleItems(R.array.sex)
                    }
                }

            btnSave.setOnClickListener {
                val name = edtName.text.toString()
                val sex = edtSex.text.toString()
                val age = edtAge.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                val inputs = listOf(name, sex, age, email)

                if (inputs.any { it.isBlank() }) {
                    Toast.makeText(this@ProfileEditActivity, "Field cannot be empty", Toast.LENGTH_SHORT).show()
                } else {
                    firestore.collection("patient")
                        .document(firebaseAuth.currentUser!!.uid).set(
                            Patient(
                                uid = firebaseAuth.currentUser!!.uid,
                                name = name,
                                sex = sex,
                                age = age.toInt(),
                                email = email
                            )
                        ).addOnFailureListener {
                            Toast.makeText(this@ProfileEditActivity, it.message, Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener {
                            if (password.isNotBlank()) {
                                firebaseAuth.currentUser?.updatePassword(password)?.addOnFailureListener {
                                    Toast.makeText(this@ProfileEditActivity, it.message, Toast.LENGTH_SHORT).show()
                                }?.addOnSuccessListener {
                                    Toast.makeText(this@ProfileEditActivity, "Update profile success", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            } else {
                                Toast.makeText(this@ProfileEditActivity, "Update profile success", Toast.LENGTH_SHORT).show()
                                finish()
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

            firestore.collection("dentist")
                .document(firebaseAuth.currentUser!!.uid)
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
                        edtClinicName.setText(user?.clinicName)
                        edtClinicAddress.setText(user?.clinicAddress)
                    }
                }

            btnSave.setOnClickListener {
                val name = edtName.text.toString()
                val city = edtCity.text.toString()
                val clinicName = edtClinicName.text.toString()
                val clinicAddress = edtClinicAddress.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                val inputs = listOf(name, city, clinicName, clinicAddress, email)

                if (inputs.any { it.isBlank() }) {
                    Toast.makeText(this@ProfileEditActivity, "Field cannot be empty", Toast.LENGTH_SHORT).show()
                } else {
                    firestore.collection("dentist")
                        .document(firebaseAuth.currentUser!!.uid).set(
                            Dentist(
                                uid = firebaseAuth.currentUser!!.uid,
                                name = name,
                                city = city,
                                clinicName = clinicName,
                                clinicAddress = clinicAddress,
                                email = email
                            )
                        ).addOnFailureListener {
                            Toast.makeText(this@ProfileEditActivity, it.message, Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener {
                            if (password.isNotBlank()) {
                                firebaseAuth.currentUser?.updatePassword(password)?.addOnFailureListener {
                                    Toast.makeText(this@ProfileEditActivity, it.message, Toast.LENGTH_SHORT).show()
                                }?.addOnSuccessListener {
                                    Toast.makeText(this@ProfileEditActivity, "Update profile success", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            } else {
                                Toast.makeText(this@ProfileEditActivity, "Update profile success", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                }
            }
        }
    }
}
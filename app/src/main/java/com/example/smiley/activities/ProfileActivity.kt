package com.example.smiley.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smiley.databinding.ActivityProfileBinding
import com.example.smiley.models.Dentist
import com.example.smiley.models.Patient
import com.example.smiley.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var prefManager: PrefManager
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this@ProfileActivity)
        firebaseAuth = FirebaseAuth.getInstance()

        with(binding) {
            FirebaseFirestore.getInstance()
                .collection(prefManager.getRole())
                .document(firebaseAuth.currentUser?.uid!!)
                .get()
                .addOnFailureListener {
                    Toast.makeText(this@ProfileActivity, it.message, Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    if (!it.exists()) {
                        Toast.makeText(this@ProfileActivity, "User not found", Toast.LENGTH_SHORT).show()
                    } else {
                        if (prefManager.getRole() == "patient") {
                            val user = it.toObject(Patient::class.java)
                            txtName.text = user?.name
                            txtEmail.text = user?.email
                            txtInfo.text = "${user?.sex}, ${user?.age}"
                        } else {
                            val user = it.toObject(Dentist::class.java)
                            txtName.text = user?.name
                            txtEmail.text = user?.email
                            txtInfo.text = "${user?.clinicName}\n${user?.clinicAddress}\n${user?.city}"
                        }
                    }
                }

            btnBack.setOnClickListener {
                finish()
            }

            btnEdit.setOnClickListener {
                val newIntent = Intent(this@ProfileActivity, ProfileEditActivity::class.java)
                startActivity(newIntent)
            }

            btnLogout.setOnClickListener {
                Toast.makeText(this@ProfileActivity, "You have been logged out", Toast.LENGTH_SHORT).show()
                prefManager.clear()
                firebaseAuth.signOut()

                val newIntent = Intent(this@ProfileActivity, WelcomeActivity::class.java)
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(newIntent)
            }
        }
    }
}
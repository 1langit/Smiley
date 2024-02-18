package com.example.smiley.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.databinding.ActivityLoginBinding
import com.example.smiley.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this@LoginActivity)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        with(binding) {
            btnLogin.setOnClickListener {
                val role = intent.getStringExtra("role")
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnFailureListener {
                    Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    firestore.collection(role!!)
                        .document(firebaseAuth.currentUser?.uid!!)
                        .get()
                        .addOnFailureListener {
                            Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener {
                            if (!it.exists()) {
                                Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                                firebaseAuth.signOut()
                            } else {
                                Toast.makeText(this@LoginActivity, "Log in success", Toast.LENGTH_SHORT).show()
                                prefManager.setLoggedIn(true)
                                prefManager.saveRole(role)

                                val newIntent = Intent(
                                    this@LoginActivity,
                                    if (role == "patient") {
                                        PatientDashboardActivity::class.java
                                    } else {
                                        DentistDashboardActivity::class.java
                                    }
                                )
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(newIntent)
                            }
                        }
                }
            }
        }
    }

    private fun login(role: String) {

    }
}
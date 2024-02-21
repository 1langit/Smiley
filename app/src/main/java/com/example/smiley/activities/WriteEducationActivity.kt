package com.example.smiley.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.smiley.R
import com.example.smiley.databinding.ActivityWriteEducationBinding
import com.example.smiley.models.Article
import com.example.smiley.models.Dentist
import com.example.smiley.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WriteEducationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteEducationBinding
    private lateinit var prefManager: PrefManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteEducationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this@WriteEducationActivity)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        var name = ""

        firestore.collection(prefManager.getRole())
            .document(firebaseAuth.currentUser?.uid!!)
            .get()
            .addOnFailureListener {
                Toast.makeText(this@WriteEducationActivity, it.message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                name = it.toObject(Dentist::class.java)!!.name
            }

        with(binding) {
            btnCancel.setOnClickListener {
                if (edtTitle.text.toString().isBlank() && edtContent.text.toString().isBlank()) {
                    finish()
                } else {
                    AlertDialog.Builder(this@WriteEducationActivity)
                        .setTitle("Exit page?")
                        .setMessage("Your changes will not be saved")
                        .setPositiveButton("Keep writing") { dialog, _ ->
                            dialog.dismiss()
                        }.setNegativeButton("Exit page") { _, _ ->
                            finish()
                        }.create().show()
                }
            }

            btnSave.setOnClickListener {
                val title = edtTitle.text.toString()
                val content = edtContent.text.toString()

                if (title.isBlank() && content.isBlank()) {
                    Toast.makeText(this@WriteEducationActivity, "Cannot cave empty article", Toast.LENGTH_SHORT).show()
                } else {
                    val article = Article(
                        writerUid = firebaseAuth.currentUser?.uid!!,
                        writerName = name,
                        title = title,
                        content = content,
                        dateCreated = LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM yyyy"))
                    )
                    firestore.collection("education").add(article).addOnFailureListener {
                        Toast.makeText(this@WriteEducationActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }.addOnSuccessListener { document ->
                        article.id = document.id
                        document.set(article).addOnFailureListener {
                            Toast.makeText(this@WriteEducationActivity, it.message, Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener {
                            Toast.makeText(this@WriteEducationActivity, "Education article created", Toast.LENGTH_SHORT).show()
                        }
                    }
                    finish()
                }
            }
        }
    }
}
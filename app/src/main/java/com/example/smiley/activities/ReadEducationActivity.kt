package com.example.smiley.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smiley.databinding.ActivityReadEducationBinding
import com.example.smiley.models.Article
import com.google.firebase.firestore.FirebaseFirestore

class ReadEducationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadEducationBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadEducationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        firestore.collection("education")
            .document(intent.getStringExtra("id")!!)
            .get()
            .addOnFailureListener {
                Toast.makeText(this@ReadEducationActivity, it.message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                val article = it.toObject(Article::class.java)!!
                with(binding) {
                    txtTitle.text = article.title
                    txtWriter.text = article.writerName
                    txtDate.text = article.dateCreated
                    txtContent.text = article.content
                }
            }
    }
}
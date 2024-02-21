package com.example.smiley.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smiley.R
import com.example.smiley.databinding.ActivityReadArticleBinding
import com.example.smiley.models.Article
import com.example.smiley.models.Dentist
import com.google.firebase.firestore.FirebaseFirestore

class ReadArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadArticleBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        firestore.collection("education")
            .document(intent.getStringExtra("id")!!)
            .get()
            .addOnFailureListener {
                Toast.makeText(this@ReadArticleActivity, it.message, Toast.LENGTH_SHORT).show()
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
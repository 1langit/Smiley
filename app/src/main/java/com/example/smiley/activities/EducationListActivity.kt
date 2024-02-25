package com.example.smiley.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smiley.databinding.ActivityEducationListBinding
import com.example.smiley.models.Article
import com.example.smiley.utils.ArticleAdapter
import com.google.firebase.firestore.FirebaseFirestore

class EducationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEducationListBinding
    private val articleCollection = FirebaseFirestore.getInstance().collection("education")
    private val articeListLiveData: MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEducationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getArticles()
        showArticles("")

        with(binding) {
            btnBack.setOnClickListener {
                finish()
            }

            boxSearch.setEndIconOnClickListener {
                showArticles(edtSearch.text.toString().lowercase())
            }
        }
    }

    private fun showArticles(keyword: String) {
        articeListLiveData.observe(this@EducationListActivity) { articleList ->
            val filteredArticleList = articleList.filter {
                it.title.lowercase().contains(keyword) || it.content.lowercase().contains(keyword)
            }.sortedBy {
                it.dateCreated
            }

            val articleAdapter = ArticleAdapter(filteredArticleList) { article ->
                val newIntent = Intent(this@EducationListActivity, ReadEducationActivity::class.java)
                newIntent.putExtra("id", article.id)
                startActivity(newIntent)
            }

            with(binding) {
                rvEducation.apply {
                    adapter = articleAdapter
                    layoutManager = LinearLayoutManager(this@EducationListActivity)
                }
                txtNotFound.visibility = if (articleList.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun getArticles() {
        articleCollection.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("Dentist list", "error listening to changes")
            }
            if (snapshots != null) {
                val articleList = snapshots.toObjects(Article::class.java)
                articeListLiveData.postValue(articleList)
            }
        }
    }
}
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

//            edtSearch.setOnEditorActionListener { _, _, _ ->
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//                    showArticles(edtSearch.text.toString())
//                    Toast.makeText(this@EducationListActivity, "jrsdrj", Toast.LENGTH_SHORT).show()
//                    true
//                } else {
//                    false
//                }
//            }
        }
    }

    private fun showArticles(searchKeyword: String) {
        articeListLiveData.observe(this@EducationListActivity) { articleList ->
//            val filteredArticleList = articleList.filter {
//                it.title.contains(searchKeyword) || it.content.contains(searchKeyword)
//            }.sortedBy {
//                it.dateCreated
//            }

            val articleAdapter = ArticleAdapter(articleList) { article ->
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
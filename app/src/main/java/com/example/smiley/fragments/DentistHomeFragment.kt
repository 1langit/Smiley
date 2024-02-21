package com.example.smiley.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smiley.activities.EducationListActivity
import com.example.smiley.activities.ProfileActivity
import com.example.smiley.activities.ReadEducationActivity
import com.example.smiley.activities.WriteEducationActivity
import com.example.smiley.databinding.FragmentDentistHomeBinding
import com.example.smiley.models.Article
import com.example.smiley.utils.ArticleAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DentistHomeFragment : Fragment() {

    private lateinit var binding: FragmentDentistHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val articleCollection = FirebaseFirestore.getInstance().collection("education")
    private val articeListLiveData: MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDentistHomeBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()

        getMyArticles()
        showMyArticles()

        with(binding) {
            btnAccount.setOnClickListener {
                val newIntent = Intent(requireContext(), ProfileActivity::class.java)
                startActivity(newIntent)
            }

            btnWrite.setOnClickListener {
                val newIntent = Intent(requireContext(), WriteEducationActivity::class.java)
                startActivity(newIntent)
            }

            btnEducation.setOnClickListener {
                val newIntent = Intent(requireContext(), EducationListActivity::class.java)
                startActivity(newIntent)
            }
        }
        return binding.root
    }

    private fun showMyArticles() {
        articeListLiveData.observe(viewLifecycleOwner) { articleList ->
            val articleAdapter = ArticleAdapter(articleList) { article ->
                val newIntent = Intent(requireContext(), ReadEducationActivity::class.java)
                newIntent.putExtra("id", article.id)
                startActivity(newIntent)
            }

            with(binding) {
                rvEducation.apply {
                    adapter = articleAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
                txtStartWrite.visibility = if (articleList.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun getMyArticles() {
        articleCollection.whereEqualTo("writerUid", firebaseAuth.currentUser?.uid!!)
            .addSnapshotListener { snapshots, error ->
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
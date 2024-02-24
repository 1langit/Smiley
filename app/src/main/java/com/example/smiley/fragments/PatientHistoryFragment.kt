package com.example.smiley.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smiley.databinding.FragmentPatientHistoryBinding
import com.example.smiley.models.Classification
import com.example.smiley.utils.CaptureHistoryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PatientHistoryFragment : Fragment() {

    private lateinit var binding: FragmentPatientHistoryBinding
    private val classificationCollection = FirebaseFirestore.getInstance().collection("classification")
    private val classificationListLiveData: MutableLiveData<List<Classification>> by lazy {
        MutableLiveData<List<Classification>>()
    }
    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientHistoryBinding.inflate(layoutInflater)
        getMyHistory()
        showHistory()
        return binding.root
    }

    private fun showHistory() {
        classificationListLiveData.observe(viewLifecycleOwner) { historyList ->

            val historyAdapter = CaptureHistoryAdapter(historyList) { history ->
                AlertDialog.Builder(requireContext())
                    .setTitle(history.classification)
                    .setMessage(history.elaboration)
                    .setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                    }.create().show()
            }

            with(binding) {
                rvClassificationHistory.apply {
                    adapter = historyAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
                txtEmpty.visibility = if (historyList.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun getMyHistory() {
        classificationCollection.whereEqualTo("uid", uid)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("list", "error listening to changes")
            }
            if (snapshots != null) {
                val historyList = snapshots.toObjects(Classification::class.java)
                classificationListLiveData.postValue(historyList)
            }
        }
    }
}
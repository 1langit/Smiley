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
import com.example.smiley.R
import com.example.smiley.activities.ChatActivity
import com.example.smiley.databinding.FragmentDentistChatBinding
import com.example.smiley.models.Dentist
import com.example.smiley.models.Patient
import com.example.smiley.utils.PatientContactAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DentistChatFragment : Fragment() {

    private lateinit var binding: FragmentDentistChatBinding
    private val patientCollection = FirebaseFirestore.getInstance().collection("patient")
    private val patientListLiveData: MutableLiveData<List<Patient>> by lazy {
        MutableLiveData<List<Patient>>()
    }
    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDentistChatBinding.inflate(layoutInflater)
        getRelatedPatient()
        showPatientList()
        return binding.root
    }

    private fun showPatientList() {
        patientListLiveData.observe(viewLifecycleOwner) { patientList ->
            val patientAdapter = PatientContactAdapter(patientList) { patient ->
                val newIntent = Intent(requireContext(), ChatActivity::class.java)
                newIntent.putExtra("id", patient.uid)
                startActivity(newIntent)
            }

            with(binding) {
                rvPatients.apply {
                    adapter = patientAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
                txtEmpty.visibility = if (patientList.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun getRelatedPatient() {
        patientCollection.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("Dentist list", "error listening to changes")
            }
            if (snapshots != null) {
                val patientList = snapshots.toObjects(Patient::class.java)
                patientListLiveData.postValue(patientList)
            }
        }
    }
}
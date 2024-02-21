package com.example.smiley.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smiley.R
import com.example.smiley.activities.ChatActivity
import com.example.smiley.databinding.FragmentPatientSearchDentistBinding
import com.example.smiley.models.Dentist
import com.example.smiley.utils.DentistContactAdapter
import com.google.firebase.firestore.FirebaseFirestore

class PatientSearchDentistFragment : Fragment() {

    private lateinit var binding: FragmentPatientSearchDentistBinding
    private val dentistCollection = FirebaseFirestore.getInstance().collection("dentist")
    private val dentistListLiveData: MutableLiveData<List<Dentist>> by lazy {
        MutableLiveData<List<Dentist>>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientSearchDentistBinding.inflate(layoutInflater)
        getAllDentist()
        showDentistList()

        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    private fun showDentistList() {
        dentistListLiveData.observe(viewLifecycleOwner) { dentistList ->
            val dentistAdapter = DentistContactAdapter(dentistList) { dentist ->
                val newIntent = Intent(requireContext(), ChatActivity::class.java)
                newIntent.putExtra("id", dentist.uid)
                startActivity(newIntent)
            }

            with(binding) {
                rvDentists.apply {
                    adapter = dentistAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
                txtEmpty.visibility = if (dentistList.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun getAllDentist() {
        dentistCollection.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("Dentist list", "error listening to changes")
            }
            if (snapshots != null) {
                val dentistList = snapshots.toObjects(Dentist::class.java)
                dentistListLiveData.postValue(dentistList)
            }
        }
    }
}
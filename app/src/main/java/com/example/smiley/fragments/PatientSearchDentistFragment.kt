package com.example.smiley.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smiley.activities.ChatActivity
import com.example.smiley.databinding.FragmentPatientSearchDentistBinding
import com.example.smiley.models.ChatRoom
import com.example.smiley.models.Dentist
import com.example.smiley.utils.DentistContactAdapter
import com.example.smiley.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PatientSearchDentistFragment : Fragment() {

    private lateinit var binding: FragmentPatientSearchDentistBinding
    private lateinit var prefManager: PrefManager
    private lateinit var firestore : FirebaseFirestore
    private val dentistListLiveData: MutableLiveData<List<Dentist>> by lazy {
        MutableLiveData<List<Dentist>>()
    }
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientSearchDentistBinding.inflate(layoutInflater)
        prefManager = PrefManager.getInstance(requireContext())
        firestore = FirebaseFirestore.getInstance()
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
//                val newIntent = Intent(requireContext(), ChatActivity::class.java)
//                newIntent.putExtra("id", dentist.uid)
//                startActivity(newIntent)

                firestore.collection("chatroom")
                    .where(
                        Filter.and(
                            Filter.equalTo("dentistUid", dentist.uid),
                            Filter.equalTo("patientUid", uid)
                        )
                    )
                    .get()
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener {
                        if (it.isEmpty) {
                            val chatRoom = ChatRoom(
                                dentistUid = dentist.uid,
                                patientUid = uid,
                                dentistName = dentist.name,
                                patientName = prefManager.getName(),
                                time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
                            )
                            firestore.collection("chatroom")
                                .add(chatRoom)
                                .addOnFailureListener {
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                                }.addOnSuccessListener { document ->
                                    document.update("id", document.id).addOnSuccessListener {
                                        val newIntent = Intent(requireContext(), ChatActivity::class.java)
                                        newIntent.putExtra("chatroomId", document.id)
                                        newIntent.putExtra("otherUserId", dentist.uid)
                                        startActivity(newIntent)
                                    }
                                }
                        } else {
                            val chatroom = it.first().toObject(ChatRoom::class.java)
                            val newIntent = Intent(requireContext(), ChatActivity::class.java)
                            newIntent.putExtra("chatroomId", chatroom.id)
                            newIntent.putExtra("otherUserId", dentist.uid)
                            startActivity(newIntent)
                        }
                    }
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
        firestore.collection("dentist").addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("Dentist list", "error listening to changes")
            }
            if (snapshots != null) {
                val dentistList = snapshots.toObjects(Dentist::class.java)
                dentistListLiveData.postValue(dentistList)
            }
        }
    }

    private fun getUserName() {

    }
}
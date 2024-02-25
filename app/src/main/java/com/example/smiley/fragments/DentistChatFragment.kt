package com.example.smiley.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smiley.activities.ChatActivity
import com.example.smiley.databinding.FragmentDentistChatBinding
import com.example.smiley.models.ChatRoom
import com.example.smiley.models.Patient
import com.example.smiley.utils.PatientContactAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DentistChatFragment : Fragment() {

    private lateinit var binding: FragmentDentistChatBinding
    private lateinit var firestore: FirebaseFirestore
    private val patientChatListLiveData: MutableLiveData<List<ChatRoom>> by lazy {
        MutableLiveData<List<ChatRoom>>()
    }
    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDentistChatBinding.inflate(layoutInflater)
        firestore = FirebaseFirestore.getInstance()
        getRelatedPatient()
        showPatientList()
        return binding.root
    }

    private fun showPatientList() {
        patientChatListLiveData.observe(viewLifecycleOwner) { patientChatList ->
            val patientAdapter = PatientContactAdapter(patientChatList) { chatroom ->
                val newIntent = Intent(requireContext(), ChatActivity::class.java)
                newIntent.putExtra("chatroomId", chatroom.id)
                newIntent.putExtra("otherUserId", chatroom.patientUid)
                startActivity(newIntent)
            }

            with(binding) {
                rvPatients.apply {
                    adapter = patientAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
                txtEmpty.visibility = if (patientChatList.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun getRelatedPatient() {
        firestore.collection("chatroom")
            .whereEqualTo("dentistUid", uid)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.d("Patient list", "error listening to changes")
                }
                if (snapshots != null) {
                    val patientList = snapshots.toObjects(ChatRoom::class.java)
                    patientChatListLiveData.postValue(patientList)
                }
        }
    }
}
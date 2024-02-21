package com.example.smiley.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smiley.activities.EducationListActivity
import com.example.smiley.activities.PatientJournalActivity
import com.example.smiley.activities.ProfileActivity
import com.example.smiley.databinding.FragmentPatientHomeBinding

class PatientHomeFragment : Fragment() {

    private lateinit var binding: FragmentPatientHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientHomeBinding.inflate(layoutInflater)

        with(binding) {
            btnAccount.setOnClickListener {
                startActivity(Intent(requireContext(), ProfileActivity::class.java))
            }

            btnDentist.setOnClickListener {
                navigateToSearchDentist()
            }

            btnEducation.setOnClickListener {
                startActivity(Intent(requireContext(), EducationListActivity::class.java))
            }

            btnJournal.setOnClickListener {
                startActivity(Intent(requireContext(), PatientJournalActivity::class.java))
            }
        }
        return binding.root
    }

    public fun navigateToSearchDentist() {
        val action = PatientHomeFragmentDirections.actionPatientHomeFragmentToPatientSearchDentistFragment()
        findNavController().navigate(action)
    }
}
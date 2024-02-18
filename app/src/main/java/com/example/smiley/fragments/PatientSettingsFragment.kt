package com.example.smiley.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smiley.databinding.FragmentPatientSettingsBinding

class PatientSettingsFragment : Fragment() {

    private lateinit var binding: FragmentPatientSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientSettingsBinding.inflate(layoutInflater)
        return binding.root
    }
}
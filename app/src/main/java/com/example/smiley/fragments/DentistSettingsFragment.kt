package com.example.smiley.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smiley.R
import com.example.smiley.databinding.FragmentDentistSettingsBinding

class DentistSettingsFragment : Fragment() {

    private lateinit var binding: FragmentDentistSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDentistSettingsBinding.inflate(layoutInflater)
        return binding.root
    }
}
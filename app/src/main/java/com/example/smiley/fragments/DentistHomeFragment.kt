package com.example.smiley.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smiley.R
import com.example.smiley.activities.ProfileActivity
import com.example.smiley.databinding.FragmentDentistHomeBinding

class DentistHomeFragment : Fragment() {

    private lateinit var binding: FragmentDentistHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDentistHomeBinding.inflate(layoutInflater)
        with(binding) {
            btnAccount.setOnClickListener {
                val newIntent = Intent(requireContext(), ProfileActivity::class.java)
                startActivity(newIntent)
            }
        }
        return binding.root
    }
}
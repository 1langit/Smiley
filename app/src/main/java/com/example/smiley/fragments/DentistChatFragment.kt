package com.example.smiley.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smiley.R
import com.example.smiley.databinding.FragmentDentistChatBinding

class DentistChatFragment : Fragment() {

    private lateinit var binding: FragmentDentistChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDentistChatBinding.inflate(layoutInflater)
        return binding.root
    }
}
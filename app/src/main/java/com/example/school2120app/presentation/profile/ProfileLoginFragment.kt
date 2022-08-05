package com.example.school2120app.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.school2120app.R
import com.example.school2120app.databinding.FragmentProfileLoginBinding

class ProfileLoginFragment: Fragment(R.layout.fragment_profile_login) {

    private lateinit var binding: FragmentProfileLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileLoginBinding.bind(view)
        binding.apply {
            btnProfileLoginIn.setOnClickListener {
                val action = ProfileLoginFragmentDirections.actionProfileFragmentToProfileDocumentsFragment()
                findNavController().navigate(action)
            }
        }
    }
}
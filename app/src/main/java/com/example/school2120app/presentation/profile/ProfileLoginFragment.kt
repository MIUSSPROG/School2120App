package com.example.school2120app.presentation.profile

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.school2120app.R
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.databinding.FragmentProfileLoginBinding
import com.example.school2120app.prefs
import com.example.school2120app.presentation.news.NewsItemFragmentArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileLoginFragment: Fragment(R.layout.fragment_profile_login) {

    private lateinit var binding: FragmentProfileLoginBinding
    private val viewModel: ProfileLoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileLoginBinding.bind(view)
        binding.apply {

            if (isUserDataSaved()){
                tvProfileLoginName.setText(prefs.login)
                tvProfileLoginPass.setText(prefs.password)
                viewModel.signIn(prefs.login!!, prefs.password!!)
            }

            btnProfileLoginIn.setOnClickListener {
                saveUserData(tvProfileLoginName.text.toString(), tvProfileLoginPass.text.toString())
                viewModel.signIn(prefs.login!!, prefs.password!!)
            }

            viewModel.signInLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Loading -> {
                        pbLogin.visibility = VISIBLE
                    }
                    is Success -> {
                        pbLogin.visibility = INVISIBLE
                        val action = ProfileLoginFragmentDirections.actionProfileFragmentToProfileDocumentsFragment(it.data!!)
                        findNavController().navigate(action)
                    }
                    is Error -> {
                        Snackbar.make(binding.root, "Ошибка входа", Snackbar.LENGTH_SHORT).show()
                        pbLogin.visibility = INVISIBLE
                    }
                }
            }
        }
    }

    private fun saveUserData(login: String, password: String){
        if (prefs.login.isNullOrBlank() && prefs.password.isNullOrBlank()){
            prefs.login = login
            prefs.password = password
        }
    }

    private fun isUserDataSaved(): Boolean{
        if (prefs.login.isNullOrBlank() && prefs.password.isNullOrBlank()){
            return false
        }
        return true
    }
}
package com.example.school2120app.presentation.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.school2120app.R
import com.example.school2120app.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment: Fragment(R.layout.fragment_schedule) {

    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var binding: FragmentScheduleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentScheduleBinding.bind(view)

        binding.btnChooseScheduleOption.setOnClickListener {
            val action = ScheduleFragmentDirections.actionScheduleFragmentToChooseOptionsFragment()
            findNavController().navigate(action)
        }
        viewModel.getSchedule("ш4")
    }
}
package com.example.school2120app.presentation.schedule

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.school2120app.R
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.databinding.FragmentScheduleBinding
import com.example.school2120app.presentation.schedule.adapter.ScheduleAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ScheduleFragment: Fragment(R.layout.fragment_schedule) {

    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var binding: FragmentScheduleBinding
    private val scheduleAdapter by lazy { ScheduleAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentScheduleBinding.bind(view)

        binding.apply {

            rvSchedule.adapter = scheduleAdapter
            btnChooseScheduleOption.setOnClickListener {
                val action = ScheduleFragmentDirections.actionScheduleFragmentToChooseOptionsFragment()
                findNavController().navigate(action)
            }
            viewModel.getSchedule(building = "ш4", grade = "5", letter = "А", weekday = "Понедельник", fetchFromRemote = false)
            swipeRefreshLayoutSchedule.setOnRefreshListener {
                viewModel.getSchedule(building = "ш4", grade = "5", letter = "А", weekday = "Понедельник", fetchFromRemote = true)
                swipeRefreshLayoutSchedule.isRefreshing = false
            }

            viewModel.scheduleData.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Success -> {
                        scheduleAdapter.submitList(it.data)
                        progressBarLoadingSchedule.visibility = INVISIBLE
                    }
                    is Resource.Error -> {
                        progressBarLoadingSchedule.visibility = VISIBLE
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is UIEvent.ShowSnackbar -> {
                            Snackbar.make(this@apply.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }
}
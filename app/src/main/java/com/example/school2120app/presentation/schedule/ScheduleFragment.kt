package com.example.school2120app.presentation.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.school2120app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment: Fragment(R.layout.fragment_schedule) {

    private val viewModel: ScheduleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSchedule()
    }
}
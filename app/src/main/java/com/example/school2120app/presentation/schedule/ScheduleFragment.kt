package com.example.school2120app.presentation.schedule

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.school2120app.R
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.data.xlsx.ScheduleParser.Companion.Friday
import com.example.school2120app.data.xlsx.ScheduleParser.Companion.Monday
import com.example.school2120app.data.xlsx.ScheduleParser.Companion.Thursday
import com.example.school2120app.data.xlsx.ScheduleParser.Companion.Tuesday
import com.example.school2120app.data.xlsx.ScheduleParser.Companion.Wednesday
import com.example.school2120app.databinding.FragmentScheduleBinding
import com.example.school2120app.prefs
import com.example.school2120app.presentation.schedule.ChooseOptionsFragment.Companion.EXTRA_BUILDING_SELECTED
import com.example.school2120app.presentation.schedule.ChooseOptionsFragment.Companion.EXTRA_GRADE_SELECTED
import com.example.school2120app.presentation.schedule.ChooseOptionsFragment.Companion.REQUEST_CODE
import com.example.school2120app.presentation.schedule.adapter.ScheduleAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class ScheduleFragment: Fragment(R.layout.fragment_schedule) {

    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var binding: FragmentScheduleBinding
    private val scheduleAdapter by lazy { ScheduleAdapter() }
    private var selectedBuilding: String = ""
    private var selectedGrade: String = ""
    private var selectedLetter: String = ""
    private var selectedWeekday: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentScheduleBinding.bind(view)

        binding.apply {

            rvSchedule.adapter = scheduleAdapter
            btnChooseScheduleOption.setOnClickListener {
                val action = ScheduleFragmentDirections.actionScheduleFragmentToChooseOptionsFragment()
                findNavController().navigate(action)
            }
            // LocalDate.now().dayOfWeek.name
            val calendar = Calendar.getInstance()
            initCardviewWeekday(calendar.get(Calendar.DAY_OF_WEEK))
            initCardViewClickedListener()

            selectedBuilding = prefs.prefBuilding ?: ""
            selectedGrade = prefs.prefGrade ?: ""
            selectedLetter = prefs.prefLetter ?: ""

            if (selectedBuilding.isNotEmpty() && selectedGrade.isNotEmpty() && selectedLetter.isNotEmpty()) {
                tvSelectedClass.visibility = VISIBLE
                tvSelectedClass.text = "Класс:  $selectedGrade-$selectedLetter-$selectedBuilding"
                viewModel.getSchedule(building = selectedBuilding, grade = selectedGrade, letter = selectedLetter, weekday = selectedWeekday, fetchFromRemote = false)
            }else{
                viewModel.loadSchedule()
            }

            viewModel.scheduleLoad.observe(viewLifecycleOwner){ result ->
                when(result){
                    is Loading -> {
                        btnChooseScheduleOption.isEnabled = false
                        progressBarLoadingSchedule.visibility = VISIBLE
                    }
                    is Success -> {
                        btnChooseScheduleOption.isEnabled = true
                        progressBarLoadingSchedule.visibility = INVISIBLE
                    }
                }
            }

            parentFragmentManager.setFragmentResultListener(REQUEST_CODE, viewLifecycleOwner){ _, data ->
                selectedBuilding = data.getString(EXTRA_BUILDING_SELECTED)!!
                selectedGrade = data.getString(EXTRA_GRADE_SELECTED)!!.split('-')[0]
                selectedLetter = data.getString(EXTRA_GRADE_SELECTED)!!.split('-')[1]
                prefs.prefBuilding = selectedBuilding
                prefs.prefGrade = selectedGrade
                prefs.prefLetter = selectedLetter

                tvSelectedClass.visibility = VISIBLE
                tvSelectedClass.text = "Класс:  $selectedGrade-$selectedLetter-$selectedBuilding"
                viewModel.getSchedule(building = selectedBuilding, grade = selectedGrade, letter = selectedLetter, weekday = selectedWeekday, fetchFromRemote = false)
            }

            swipeRefreshLayoutSchedule.setOnRefreshListener {
                viewModel.loadSchedule()
                viewModel.getSchedule(building = selectedBuilding, grade = selectedGrade, letter = selectedLetter, weekday = selectedWeekday, fetchFromRemote = true)
                swipeRefreshLayoutSchedule.isRefreshing = false
            }

            viewModel.scheduleData.observe(viewLifecycleOwner){
                when(it){
                    is Loading -> {
                        progressBarLoadingSchedule.visibility = VISIBLE
                    }
                    is Success -> {
                        scheduleAdapter.submitList(it.data)
                        progressBarLoadingSchedule.visibility = INVISIBLE
                        if (it.data!!.isEmpty()){
                            imgvImagePreview.visibility = VISIBLE
                        }else {
                            imgvImagePreview.visibility = INVISIBLE
                        }
                    }
                    is Error -> {
                        progressBarLoadingSchedule.visibility = INVISIBLE
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is UIEvent.ShowSnackbar -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun initCardviewWeekday(curWeekday: Int){
        binding.apply {
            when(curWeekday){
                Calendar.MONDAY -> {
                    selectedWeekday = Monday
                    cardViewMonday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                    tvMonday.setTextColor(Color.WHITE)
                }
                Calendar.TUESDAY -> {
                    selectedWeekday = Tuesday
                    cardViewTuesday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                    tvTuesday.setTextColor(Color.WHITE)
                }
                Calendar.WEDNESDAY -> {
                    selectedWeekday = Wednesday
                    cardViewWednesday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                    tvWednesday.setTextColor(Color.WHITE)
                }
                Calendar.THURSDAY -> {
                    selectedWeekday = Thursday
                    cardViewThursday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                    tvThursday.setTextColor(Color.WHITE)
                }
                Calendar.FRIDAY -> {
                    selectedWeekday = Friday
                    cardViewFriday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                    tvFriday.setTextColor(Color.WHITE)
                }
            }
        }
    }

    private fun initCardViewClickedListener(){
        binding.apply {

            cardViewMonday.setOnClickListener {
                clearCardViewClicked()
                cardViewMonday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                tvMonday.setTextColor(Color.WHITE)
                selectedWeekday = Monday
                viewModel.getSchedule(building = selectedBuilding, grade = selectedGrade, letter = selectedLetter, weekday = selectedWeekday, fetchFromRemote = false)
            }
            cardViewTuesday.setOnClickListener {
                clearCardViewClicked()
                cardViewTuesday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                tvTuesday.setTextColor(Color.WHITE)
                selectedWeekday = Tuesday
                viewModel.getSchedule(building = selectedBuilding, grade = selectedGrade, letter = selectedLetter, weekday = selectedWeekday, fetchFromRemote = false)
            }
            cardViewWednesday.setOnClickListener {
                clearCardViewClicked()
                cardViewWednesday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                tvWednesday.setTextColor(Color.WHITE)
                selectedWeekday = Wednesday
                viewModel.getSchedule(building = selectedBuilding, grade = selectedGrade, letter = selectedLetter, weekday = selectedWeekday, fetchFromRemote = false)
            }
            cardViewThursday.setOnClickListener {
                clearCardViewClicked()
                cardViewThursday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                tvThursday.setTextColor(Color.WHITE)
                selectedWeekday = Thursday
                viewModel.getSchedule(building = selectedBuilding, grade = selectedGrade, letter = selectedLetter, weekday = selectedWeekday, fetchFromRemote = false)
            }
            cardViewFriday.setOnClickListener {
                clearCardViewClicked()
                cardViewFriday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_dark))
                tvFriday.setTextColor(Color.WHITE)
                selectedWeekday = Friday
                viewModel.getSchedule(building = selectedBuilding, grade = selectedGrade, letter = selectedLetter, weekday = selectedWeekday, fetchFromRemote = false)
            }
        }
    }

    private fun clearCardViewClicked(){
        binding.apply {
            cardViewMonday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_light))
            cardViewTuesday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_light))
            cardViewWednesday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_light))
            cardViewThursday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_light))
            cardViewFriday.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_light))

            tvMonday.setTextColor(resources.getColor(R.color.blue_dark))
            tvTuesday.setTextColor(resources.getColor(R.color.blue_dark))
            tvWednesday.setTextColor(resources.getColor(R.color.blue_dark))
            tvThursday.setTextColor(resources.getColor(R.color.blue_dark))
            tvFriday.setTextColor(resources.getColor(R.color.blue_dark))
        }
    }
}
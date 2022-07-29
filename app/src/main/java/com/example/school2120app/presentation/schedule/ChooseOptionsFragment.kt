package com.example.school2120app.presentation.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.databinding.FragmentChooseOptionsBinding
import com.example.school2120app.prefs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseOptionsFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChooseOptionsBinding
    private val viewModel: ChooseOptionsViewModel by viewModels()
    private lateinit var buildings: List<String>
    private lateinit var grades: List<String>
    private var selectedBuilding = ""
    private var selectedGrade = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseOptionsBinding.inflate(inflater, container, false)

        binding.apply {
            viewModel.getBuildings()

            viewModel.buildingsLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Success -> {
                        buildings = it.data!!
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, buildings)
                        dropDownBuildings.setAdapter(adapter)
                    }
                }
            }
            viewModel.gradesLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Success -> {
                        grades = it.data!!
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, grades)
                        dropDownGrades.setAdapter(adapter)
                    }
                }
            }

            dropDownBuildings.setOnItemClickListener { parent, view, position, id ->
                selectedBuilding = buildings[position]
                viewModel.getGrades(selectedBuilding)
            }

            dropDownGrades.setOnItemClickListener { parent, view, position, id ->
                selectedGrade = grades[position]
            }

            btnShowSchedule.setOnClickListener {
                parentFragmentManager.setFragmentResult(REQUEST_CODE, bundleOf(
                    EXTRA_BUILDING_SELECTED to selectedBuilding, EXTRA_GRADE_SELECTED to selectedGrade))
                dismiss()
            }
        }

        return binding.root
    }

    companion object{
        const val REQUEST_CODE = "SHOW_SCHEDULE"
        const val EXTRA_BUILDING_SELECTED = "EXTRA_BUILDING_SELECTED"
        const val EXTRA_GRADE_SELECTED = "EXTRA_GRADE_SELECTED"
    }
}
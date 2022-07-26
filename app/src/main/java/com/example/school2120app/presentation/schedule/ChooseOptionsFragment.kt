package com.example.school2120app.presentation.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.school2120app.databinding.FragmentChooseOptionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseOptionsFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChooseOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseOptionsBinding.inflate(inflater, container, false)

        return binding.root
    }
}
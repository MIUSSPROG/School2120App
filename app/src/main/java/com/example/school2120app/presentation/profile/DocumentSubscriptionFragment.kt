package com.example.school2120app.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.school2120app.R
import com.example.school2120app.databinding.FragmentDocumentSubscriptionBinding

class DocumentSubscriptionFragment(val text: String): Fragment(R.layout.fragment_document_subscription) {

    lateinit var binding: FragmentDocumentSubscriptionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDocumentSubscriptionBinding.bind(view)
        binding.tvDoc.text = text

    }
}
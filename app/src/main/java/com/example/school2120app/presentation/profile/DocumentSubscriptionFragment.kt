package com.example.school2120app.presentation.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.school2120app.R
import com.example.school2120app.databinding.FragmentDocumentSubscriptionBinding
import com.example.school2120app.domain.model.profile.UserDoc

class DocumentSubscriptionFragment(docs: List<Map<String, UserDoc>>) : Fragment(R.layout.fragment_document_subscription) {

    private lateinit var binding: FragmentDocumentSubscriptionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDocumentSubscriptionBinding.bind(view)

    }
}
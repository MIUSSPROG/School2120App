package com.example.school2120app.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.school2120app.R
import com.example.school2120app.core.util.ActionListener
import com.example.school2120app.databinding.FragmentDocumentSubscriptionBinding
import com.example.school2120app.domain.model.profile.UserDoc
import com.example.school2120app.presentation.profile.adapter.DocumentsAdapter

class DocumentSubscriptionFragment(val docs: MutableList<UserDoc>) : Fragment(R.layout.fragment_document_subscription) {

    private lateinit var binding: FragmentDocumentSubscriptionBinding
    private val adapter by lazy { DocumentsAdapter(object : ActionListener<UserDoc> {
        override fun onItemClicked(item: UserDoc, view: View?) {
            val action = ProfileDocumentsFragmentDirections.actionProfileDocumentsFragmentToDocumentSubscriptionDetailFragment(item)
            findNavController().navigate(action)
        }
    }) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDocumentSubscriptionBinding.bind(view)
        binding.apply {
            rvDocs.adapter = adapter
            adapter.submitList(docs)
        }

    }
}
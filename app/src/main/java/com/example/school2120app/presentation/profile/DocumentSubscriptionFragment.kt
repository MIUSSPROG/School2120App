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

class DocumentSubscriptionFragment() : Fragment(R.layout.fragment_document_subscription) {

    private lateinit var binding: FragmentDocumentSubscriptionBinding
    private val adapter by lazy { DocumentsAdapter(object : ActionListener<UserDoc> {
        override fun onItemClicked(item: UserDoc, view: View?) {
            val action = ProfileDocumentsFragmentDirections.actionProfileDocumentsFragmentToDocumentSubscriptionDetailFragment(item)
            findNavController().navigate(action)
        }
    })
    }
    private var docs: ArrayList<UserDoc>? = null

    constructor(docs: ArrayList<UserDoc>): this(){
        this.docs = docs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            docs = savedInstanceState.getParcelableArrayList(SAVED_DOCUMENTS)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDocumentSubscriptionBinding.bind(view)
        binding.apply {
            rvDocs.adapter = adapter
            adapter.submitList(docs)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_DOCUMENTS, docs)
    }

    companion object{
        const val SAVED_DOCUMENTS = "saved_documents"
    }
}
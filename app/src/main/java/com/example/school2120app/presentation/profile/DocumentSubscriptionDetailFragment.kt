package com.example.school2120app.presentation.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.school2120app.R
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.databinding.FragmentDocumentSubscriptionDetailBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DocumentSubscriptionDetailFragment: Fragment(R.layout.fragment_document_subscription_detail) {

    private lateinit var binding: FragmentDocumentSubscriptionDetailBinding
    private val args: DocumentSubscriptionDetailFragmentArgs by navArgs()
    private val viewModel: DocumentSubscriptionDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDocumentSubscriptionDetailBinding.bind(view)

        binding.apply {
            viewModel.downloadDocument(url = args.userDocToSubscribe.fileUrl)
            viewModel.downloadDocumentLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Loading -> {
                        pbDownloadDocument.visibility = View.VISIBLE
                    }
                    is Success -> {
                        pdfViewer.fromBytes(it.data).load()
                        pbDownloadDocument.visibility = View.INVISIBLE
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is UIEvent.ShowSnackbar -> {
                            pbDownloadDocument.visibility = View.INVISIBLE
                            Snackbar.make(this@apply.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
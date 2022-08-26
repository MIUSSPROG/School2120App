package com.example.school2120app.presentation.profile

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.school2120app.R
import com.example.school2120app.core.util.Resource.Loading
import com.example.school2120app.core.util.Resource.Success
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.core.util.toBoolean
import com.example.school2120app.databinding.FragmentDocumentSubscriptionDetailBinding
import com.example.school2120app.prefs
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

            viewModel.documentSubscriptionLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Loading -> {
                        pbDownloadDocument.visibility = View.VISIBLE
                    }
                    is Success -> {
                        pbDownloadDocument.visibility = View.INVISIBLE
                        Snackbar.make(binding.root, it.data!!, Snackbar.LENGTH_SHORT).show()
                        viewModel.updateDocuments(login = prefs.login!!, password = prefs.password!!)
                    }
                }
            }

            viewModel.updatedDocumentsLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Success -> {
                        val action = DocumentSubscriptionDetailFragmentDirections.actionDocumentSubscriptionDetailFragmentToProfileDocumentsFragment(it.data)
                        findNavController().navigate(action)
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is UIEvent.ShowSnackbar -> {
                            pbDownloadDocument.visibility = View.INVISIBLE
                            Snackbar.make(this@apply.root, event.message, Snackbar.LENGTH_SHORT).show()
                            btnSubscribeDoc.isEnabled = false
                            btnRefuseDoc.isEnabled = false
                        }
                    }
                }
            }

            btnRefuseDoc.setOnClickListener {
                viewModel.subscribeDocument(url = args.userDocToSubscribe.unSubLink)
            }

            btnSubscribeDoc.setOnClickListener {
                val promptInfo = PromptInfo.Builder()
                    .setTitle("Подписание документа")
                    .setNegativeButtonText("Использовать Логин и Пароль")
                    .build()

                val executor = ContextCompat.getMainExecutor(requireContext());

                val biometricPrompt = BiometricPrompt(requireActivity(), executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            val action = DocumentSubscriptionDetailFragmentDirections.actionDocumentSubscriptionDetailFragmentToDialogSubscriptionFragment(args.userDocToSubscribe)
                            findNavController().navigate(action)
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            viewModel.subscribeDocument(url = args.userDocToSubscribe.subLink)
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                        }
                    })

                biometricPrompt.authenticate(promptInfo)
            }

            if (args.userDocToSubscribe.isSubscribe.toBoolean() && !args.userDocToSubscribe.isUnsubscribe.toBoolean()){
                btnSubscribeDoc.visibility = INVISIBLE
                btnRefuseDoc.visibility = INVISIBLE
            }
        }
    }
}
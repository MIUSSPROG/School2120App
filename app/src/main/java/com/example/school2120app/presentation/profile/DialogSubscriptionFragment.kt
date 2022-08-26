package com.example.school2120app.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.databinding.FragmentDialogSubscribeDocBinding
import com.example.school2120app.prefs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DialogSubscriptionFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogSubscribeDocBinding
    private val viewModel: DialogSubscriptionViewModel by viewModels()
    private val args: DialogSubscriptionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogSubscribeDocBinding.inflate(inflater, container, false)

        binding.apply {
            btnDialogConfirmDoc.setOnClickListener {
                viewModel.subscribeDocument(url = args.userDoc.subLink)
            }

            btnDialogRejectDoc.setOnClickListener {
//                viewModel.subscribeDocument(url = args.userDoc.unSubLink)
                dismiss()
            }

            viewModel.subscriptionDocumentLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Loading -> {
                        pbSubDocumentLoading.visibility = View.VISIBLE
                    }
                    is Success -> {
                        pbSubDocumentLoading.visibility = View.INVISIBLE
                        Snackbar.make(binding.root, it.data!!, Snackbar.LENGTH_SHORT).show()
                        viewModel.fetchDocuments(login = prefs.login!!, password = prefs.password!!)
                    }
                }
            }

            viewModel.fetchDocumentsLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Success -> {
                        val action = DialogSubscriptionFragmentDirections.actionDialogSubscriptionFragmentToProfileDocumentsFragment(it.data)
                        findNavController().navigate(action)
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is UIEvent.ShowSnackbar -> {
                            Snackbar.make(this@apply.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        return binding.root
    }
}
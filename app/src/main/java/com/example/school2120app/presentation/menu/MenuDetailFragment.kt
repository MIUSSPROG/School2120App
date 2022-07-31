package com.example.school2120app.presentation.menu

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.school2120app.R
import com.example.school2120app.core.util.Resource.Loading
import com.example.school2120app.core.util.Resource.Success
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.databinding.FragmentMenuDetailBinding
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MenuDetailFragment: Fragment(R.layout.fragment_menu_detail) {

    private lateinit var binding: FragmentMenuDetailBinding
    private val args: MenuDetailFragmentArgs by navArgs()
    private var menuItem: MenuItem? = null
    private val viewModel: MenuDetailViewModel by viewModels()
    private val requestWriteStoragePermissionLauncher = registerForActivityResult(
        RequestPermission(),
        ::onGotWriteStoragePermission
    )

    private fun onGotWriteStoragePermission(granted: Boolean){
        if (granted){
            downloadFile()
        }else{
            Toast.makeText(requireContext(), "Разрешение отклонено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuDetailBinding.bind(view)
        menuItem = args.menuPdfItem

        binding.apply {

            zoomMenuPreview.setOnTouchListener { v, event ->
                v.visibility = INVISIBLE
                return@setOnTouchListener true
            }
            tvMenuName.text = menuItem!!.name.substring(startIndex = 0, endIndex = menuItem!!.name.lastIndexOf('.'))
            viewModel.getPreview(menuItem!!.previewUrl)
            viewModel.previewImageSourceLiveData.observe(viewLifecycleOwner){
                when(it){
                    is Success -> {
                        pbMenuDownloading.visibility = INVISIBLE
                        imgvMenuFile.setImage(it.data!!)
                    }
                    is Loading -> {
                        pbMenuDownloading.visibility = VISIBLE
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is UIEvent.ShowSnackbar -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                            pbMenuDownloading.visibility = INVISIBLE
                        }
                    }
                }
            }

            btnDownloadMenu.setOnClickListener {
                requestWriteStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun downloadFile(){
        Toast.makeText(requireContext(), "Загрузка...", Toast.LENGTH_SHORT).show()
        val request = DownloadManager.Request(Uri.parse(menuItem!!.downloadUrl))
        request.apply {
            setTitle(menuItem!!.name)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalFilesDir(requireContext(), Environment.DIRECTORY_DOWNLOADS, menuItem!!.name)
            setMimeType("application/pdf")
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            viewModel.downloadFile(downloadManager, request)
        }
    }
}
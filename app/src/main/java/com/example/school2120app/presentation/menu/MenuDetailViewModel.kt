package com.example.school2120app.presentation.menu

import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.domain.usecase.GetPreviewUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(private val getPreviewUsecase: GetPreviewUsecase): ViewModel() {

    private val _previewImageSourceLiveData = MutableLiveData<Resource<ImageSource>>()
    val previewImageSourceLiveData: LiveData<Resource<ImageSource>> = _previewImageSourceLiveData

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getPreview(previewUrl: String){
        viewModelScope.launch {
            getPreviewUsecase(previewUrl).onEach { result ->
                when(result){
                    is Success -> {
                        _previewImageSourceLiveData.postValue(Success(data = result.data))
                    }
                    is Loading -> {
                        _previewImageSourceLiveData.postValue(Loading())
                    }
                    is Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "Ошибка подгрузки превью меню"))
                    }
                }
            }.launchIn(this)
        }
    }

    fun downloadFile(dm: DownloadManager, request: DownloadManager.Request){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dm.enqueue(request)
            }catch (e: Exception){
                _eventFlow.emit(UIEvent.ShowSnackbar("Ошибка загрузки файла"))
                Log.d("Error", e.message.toString())
            }
        }
    }
}
package com.example.school2120app.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.domain.model.profile.ProfileDocs
import com.example.school2120app.domain.usecase.DownloadDocumentUsecase
import com.example.school2120app.domain.usecase.SignInUsecase
import com.example.school2120app.domain.usecase.SubscribeDocumentUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentSubscriptionDetailViewModel @Inject constructor(
    private val downloadDocumentUsecase: DownloadDocumentUsecase,
    private val subscribeDocumentUsecase: SubscribeDocumentUsecase,
    private val signInUsecase: SignInUsecase
    ): ViewModel() {

    private val _downloadDocumentLiveData = MutableLiveData<Resource<ByteArray>>()
    val downloadDocumentLiveData: LiveData<Resource<ByteArray>> = _downloadDocumentLiveData

    private val _documentSubscriptionLiveData = MutableLiveData<Resource<String>>()
    val documentSubscriptionLiveData: LiveData<Resource<String>> = _documentSubscriptionLiveData

    private val _updatedDocumentsLiveData = MutableLiveData<Resource<ProfileDocs>>()
    val updatedDocumentsLiveData: LiveData<Resource<ProfileDocs>> = _updatedDocumentsLiveData

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun downloadDocument(url: String){
        viewModelScope.launch {
            downloadDocumentUsecase(url).onEach {
                when(it){
                    is Loading -> {
                        _downloadDocumentLiveData.postValue(Loading())
                    }
                    is Success -> {
                        _downloadDocumentLiveData.postValue(Success(data = it.data))
                    }
                    is Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(it.message ?: "Неизвестная ошибка"))
                    }
                }
            }.launchIn(this)
        }
    }

    fun subscribeDocument(url: String){
        viewModelScope.launch {
            subscribeDocumentUsecase(url).onEach {
                when(it){
                    is Loading -> {
                        _documentSubscriptionLiveData.postValue(Loading())
                    }
                    is Success -> {
                        _documentSubscriptionLiveData.postValue(Success(data = it.data))
                    }
                    is Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(it.message ?: "Неизвестная ошибка"))
                    }
                }
            }.launchIn(this)
        }
    }

    fun updateDocuments(login: String, password: String){
        viewModelScope.launch {
            signInUsecase(login, password).onEach {
                when(it){
                    is Success -> {
                        _updatedDocumentsLiveData.postValue(Success(data = it.data))
                    }
                    is Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(it.message ?: "Неизвестная ошибка"))
                    }
                }
            }.launchIn(this)
        }
    }
}
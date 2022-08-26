package com.example.school2120app.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.domain.model.profile.ProfileDocs
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
class DialogSubscriptionViewModel @Inject constructor(
    private val subscribeDocumentUsecase: SubscribeDocumentUsecase,
    private val signInUsecase: SignInUsecase
) : ViewModel() {

    private val _subscriptionDocumentLiveData = MutableLiveData<Resource<String>>()
    val subscriptionDocumentLiveData: LiveData<Resource<String>> = _subscriptionDocumentLiveData

    private val _fetchDocumentsLiveData = MutableLiveData<Resource<ProfileDocs>>()
    val fetchDocumentsLiveData: LiveData<Resource<ProfileDocs>> = _fetchDocumentsLiveData

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun subscribeDocument(url: String) {
        viewModelScope.launch {
            subscribeDocumentUsecase(link = url).onEach {
                when (it) {
                    is Loading -> {
                        _subscriptionDocumentLiveData.postValue(Loading())
                    }
                    is Success -> {
                        _subscriptionDocumentLiveData.postValue(Success(data = it.data))
                    }
                    is Error -> {
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                it.message ?: "Неизвестная ошибка"
                            )
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun fetchDocuments(login: String, password: String){
        viewModelScope.launch {
            signInUsecase(login, password).onEach {
                when(it){
                    is Loading -> {
                        _fetchDocumentsLiveData.postValue(Loading())
                    }
                    is Success -> {
                        _fetchDocumentsLiveData.postValue(Success(data = it.data))
                    }
                    is Error -> {
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                it.message ?: "Неизвестная ошибка"
                            )
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}
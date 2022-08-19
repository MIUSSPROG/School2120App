package com.example.school2120app.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.domain.model.profile.ProfileDocs
import com.example.school2120app.domain.model.profile.ProfileInfo
import com.example.school2120app.domain.usecase.SignInUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Error

@HiltViewModel
class ProfileLoginViewModel @Inject constructor(
    private val signInUsecase: SignInUsecase
): ViewModel() {

    private val _signInLiveData = MutableLiveData<Resource<ProfileDocs>>()
    val signInLiveData: LiveData<Resource<ProfileDocs>> = _signInLiveData

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun signIn(login: String, password: String){
        viewModelScope.launch {
            signInUsecase(login, password).onEach {
                when(it){
                    is Loading -> {
                        _signInLiveData.postValue(Loading())
                    }
                    is Success -> {
                        _signInLiveData.postValue(Success(data = it.data))
                    }
                    is Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(it.message ?: "Неизвестная ошибка"))
                    }
                }
            }.launchIn(this)
        }
    }
}
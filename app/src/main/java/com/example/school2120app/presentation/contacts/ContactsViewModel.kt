package com.example.school2120app.presentation.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.domain.usecase.GetContactsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUsecase: GetContactsUsecase
): ViewModel() {

    private val _contactsLiveData = MutableLiveData<Resource<List<ContactInfo>>>()
    val contactsLiveData: LiveData<Resource<List<ContactInfo>>> = _contactsLiveData

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getContacts(){
        viewModelScope.launch {
            getContactsUsecase().onEach {
                when(it){
                    is Loading -> {
                        _contactsLiveData.postValue(Loading())
                    }
                    is Success -> {
                        _contactsLiveData.postValue(Success(data = it.data))
                    }
                    is Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(it.message ?: "Неизвестная ошибка"))
                    }
                }
            }.launchIn(this)
        }
    }
}
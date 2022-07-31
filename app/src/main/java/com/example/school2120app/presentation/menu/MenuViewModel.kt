package com.example.school2120app.presentation.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.example.school2120app.domain.usecase.GetMenusUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val getMenusUsecase: GetMenusUsecase): ViewModel() {

    private val _menusLiveData = MutableLiveData<Resource<List<MenuItem>>>()
    val menusLiveData: LiveData<Resource<List<MenuItem>>> = _menusLiveData

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getMenus(fetchFromRemote: Boolean){
        viewModelScope.launch {
            getMenusUsecase(fetchFromRemote).onEach { result ->
                when(result){
                    is Loading -> {
                        _menusLiveData.postValue(Loading())
                    }
                    is Success -> {
                        _menusLiveData.postValue(Success(data = result.data))
                    }
                    is Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "Неизвестная ошибка"))
                    }
                }
            }.launchIn(this)
        }
    }
}
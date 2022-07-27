package com.example.school2120app.presentation.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import com.example.school2120app.domain.usecase.GetScheduleUsecase
import com.example.school2120app.presentation.news.NewsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val getScheduleUsecase: GetScheduleUsecase): ViewModel() {

    private val _scheduleData = MutableLiveData<Resource<ScheduleByBuilding>>()
    val scheduleData: LiveData<Resource<ScheduleByBuilding>> = _scheduleData

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getSchedule(building: String){
        viewModelScope.launch {
            getScheduleUsecase(building).onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _scheduleData.postValue(Resource.Success(result.data))
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "Неизвестная ошибка"))
                    }
                    is Resource.Loading -> {
                        _scheduleData.postValue(Resource.Loading())
                    }
                }

            }.launchIn(this)
        }
    }
}
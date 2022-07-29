package com.example.school2120app.presentation.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.domain.model.schedule.local.GradeLesson
import com.example.school2120app.domain.usecase.GetScheduleUsecase
import com.example.school2120app.domain.usecase.LoadScheduleUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getScheduleUsecase: GetScheduleUsecase,
    private val loadScheduleUsecase: LoadScheduleUsecase
): ViewModel() {

    private val _scheduleData = MutableLiveData<Resource<List<GradeLesson>>>()
    val scheduleData: LiveData<Resource<List<GradeLesson>>> = _scheduleData

    private val _scheduleLoad = MutableLiveData<Resource<Unit>>()
    val scheduleLoad: LiveData<Resource<Unit>> = _scheduleLoad

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getSchedule(building: String, grade: String, letter: String, weekday: String, fetchFromRemote: Boolean){
        viewModelScope.launch {
            getScheduleUsecase(building = building, grade = grade, letter = letter, weekday = weekday, fetchFromRemote = fetchFromRemote).onEach { result ->
                when(result){
                    is Success -> {
                        _scheduleData.postValue(Success(result.data))
                    }
                    is Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "Неизвестная ошибка"))
                    }
                    is Loading -> {
                        _scheduleData.postValue(Loading())
                    }
                }

            }.launchIn(this)
        }
    }

    fun loadSchedule(){
        viewModelScope.launch {
            loadScheduleUsecase().onEach { result ->
                when(result){
                    is Success -> {
                        _scheduleLoad.postValue(Success(data = null))
                    }
                    is Loading -> {
                        _scheduleLoad.postValue(Loading())
                    }
                    is Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar("Ошибка подгрузки данных ${result.message}"))
                    }
                }
            }.launchIn(this)
        }
    }
}
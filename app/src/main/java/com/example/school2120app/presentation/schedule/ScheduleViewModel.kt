package com.example.school2120app.presentation.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.usecase.GetScheduleUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val getScheduleUsecase: GetScheduleUsecase): ViewModel() {

    fun getSchedule(){
        viewModelScope.launch {
            getScheduleUsecase().onEach { result ->
                when(result){
                    is Resource.Success -> {
                        Log.d("result", result.data.toString())
                    }
                }

            }.launchIn(this)
        }
    }
}
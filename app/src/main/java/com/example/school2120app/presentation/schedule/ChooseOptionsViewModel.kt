package com.example.school2120app.presentation.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.UIEvent
import com.example.school2120app.domain.usecase.GetScheduleBuildingsUsecase
import com.example.school2120app.domain.usecase.GetScheduleGradesUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseOptionsViewModel @Inject constructor(
    private val getScheduleBuildingsUsecase: GetScheduleBuildingsUsecase,
    private val getScheduleGradesUsecase: GetScheduleGradesUsecase
    ): ViewModel() {

        private val _buildingsLiveData = MutableLiveData<Resource<List<String>>>()
        val buildingsLiveData: LiveData<Resource<List<String>>> = _buildingsLiveData

        private val _gradesLiveData = MutableLiveData<Resource<List<String>>>()
        val gradesLiveData: LiveData<Resource<List<String>>> = _gradesLiveData

        private val _eventFlow = MutableSharedFlow<UIEvent>()
        val eventFlow = _eventFlow.asSharedFlow()

        fun getBuildings(){
            viewModelScope.launch {
                getScheduleBuildingsUsecase().onEach {  result ->
                    when(result){
                        is Success -> {
                            _buildingsLiveData.postValue(Success(data = result.data))
                        }
                        is Error -> {
                            _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "Неизвестная ошибка"))
                        }
                    }
                }.launchIn(this)
            }
        }

        fun getGrades(building: String){
            viewModelScope.launch {
                getScheduleGradesUsecase(building).onEach {  result ->
                    when(result){
                        is Success -> {
                            _gradesLiveData.postValue(Success(data = result.data))
                        }
                        is Error -> {
                            _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "Неизвестная ошибка"))
                        }
                    }
                }.launchIn(this)
            }
        }

}
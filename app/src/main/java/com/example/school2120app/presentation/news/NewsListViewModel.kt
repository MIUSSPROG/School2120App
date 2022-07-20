package com.example.school2120app.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.usecase.GetNewsListUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(private val getNewsListUsecase: GetNewsListUsecase): ViewModel() {

    private val _newsListLiveData = MutableLiveData<Resource<List<News>>>()
    val newsListLiveData : LiveData<Resource<List<News>>> = _newsListLiveData

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun getNews(count: Int = 100, query: String? = null){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            getNewsListUsecase(count, query).onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _newsListLiveData.postValue(Resource.Success(result.data))
                    }
                    is Resource.Loading -> {
                        _newsListLiveData.postValue(Resource.Loading())
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(
                            result.message ?: "Неизвестная ошибка"
                        ))
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class UIEvent{
        data class ShowSnackbar(val message: String): UIEvent()
    }

}
package com.example.school2120app.core.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HelperMethods(
    private val dispatchers: DispatcherProvider
) {
    fun getCountDownFlow(): Flow<Int> = flow {
        val startingValue = 5
        var currentValue = startingValue
        emit(startingValue)
        while (currentValue > 0){
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }.flowOn(dispatchers.main)
}
package com.example.school2120app.core.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class HelperMethods(
    private val dispatchers: DispatcherProvider? = null
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
    }.flowOn(dispatchers!!.main)

    companion object {
        fun isDateToCome(source: String): Boolean {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(source, formatter).toEpochDay()
            val now = LocalDate.now().toEpochDay()
            return date - now >= 0
        }
    }
}
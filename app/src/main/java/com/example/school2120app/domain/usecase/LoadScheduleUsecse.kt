package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class LoadScheduleUsecase(private val repository: MainRepository) {
    operator fun invoke(): Flow<Resource<Unit>> {
        return repository.loadSchedule()
    }
}
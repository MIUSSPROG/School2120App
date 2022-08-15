package com.example.school2120app.domain.usecase

import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class GetCountDownFlowUsecase(val repository: MainRepository) {
    operator fun invoke(): Flow<Int>{
        return repository.getCountDownFlow()
    }
}
package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class GetScheduleGradesUsecase(private val repository: MainRepository) {
    operator fun invoke(building: String): Flow<Resource<List<String>>>{
        return repository.getGrades(building)
    }
}
package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class GetScheduleBuildingsUsecase(private val repository: MainRepository) {
    operator fun invoke(): Flow<Resource<List<String>>> {
        return repository.getBuildings()
    }
}
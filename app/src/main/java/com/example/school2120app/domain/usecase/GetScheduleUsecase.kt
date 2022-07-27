package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import com.example.school2120app.domain.model.schedule.remote.ScheduleItem
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class GetScheduleUsecase(private val repository: MainRepository) {
    operator fun invoke(building: String): Flow<Resource<ScheduleByBuilding>> {
        return repository.getSchedule(building)
    }
}
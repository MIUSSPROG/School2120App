package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.schedule.local.GradeLesson
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class GetScheduleUsecase(private val repository: MainRepository) {
    operator fun invoke(building: String, grade: String, letter: String, weekday: String, fetchFromRemote: Boolean): Flow<Resource<List<GradeLesson>>> {
        return repository.getSchedule(building = building, grade = grade, letter = letter, weekday = weekday, fetchFromRemote = fetchFromRemote)
    }
}
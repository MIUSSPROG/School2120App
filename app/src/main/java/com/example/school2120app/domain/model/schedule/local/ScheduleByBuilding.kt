package com.example.school2120app.domain.model.schedule.local

data class ScheduleByBuilding(
    val building: String,
    val scheduleList: List<Schedule>
)

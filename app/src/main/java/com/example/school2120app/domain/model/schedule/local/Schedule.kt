package com.example.school2120app.domain.model.schedule.local

data class Schedule(
    val grade: String,
    val letter: String,
    val weekdays: List<Weekday>
)

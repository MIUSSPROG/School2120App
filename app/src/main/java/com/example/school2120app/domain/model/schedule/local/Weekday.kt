package com.example.school2120app.domain.model.schedule.local

data class Weekday(
    val weekday: String,
    val lessons: List<LessonInfo>
)

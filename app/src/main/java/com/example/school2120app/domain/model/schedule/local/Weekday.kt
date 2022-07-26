package com.example.school2120app.domain.model.schedule.local

data class Weekday(
    val weekdayLessons: Map<String, MutableList<LessonInfo>>
//    val weekday: String,
//    var lessons: MutableList<LessonInfo>? = null
)

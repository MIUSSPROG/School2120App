package com.example.school2120app.domain.model.schedule.local

data class Schedule(
    val grade: String,
    val letter: String,
    var weekdayLessons: MutableMap<String, MutableList<LessonInfo>>? = null
//    var weekdays: MutableList<Weekday>? = null
)

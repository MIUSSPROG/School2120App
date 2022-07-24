package com.example.school2120app.data.remote.schedule.dto

import com.google.gson.annotations.SerializedName


data class ScheduleDto(
    @SerializedName("items")
    val scheduleItems: List<ScheduleItemDto>,
    val limit: Int,
    val offset: Int
)
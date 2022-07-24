package com.example.school2120app.data.remote.schedule.dto


import com.google.gson.annotations.SerializedName

data class ExifDto(
    @SerializedName("date_time")
    val dateTime: String
)
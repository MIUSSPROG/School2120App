package com.example.school2120app.data.remote.schedule

import com.example.school2120app.data.remote.schedule.dto.ScheduleDto
import com.example.school2120app.data.remote.schedule.dto.ScheduleItemDto
import okhttp3.ResponseBody
import retrofit2.http.Header
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ScheduleApi {

    @GET("/v1/disk/resources/files")
    suspend fun getAllFiles(@Header("Authorization") token: String): ScheduleDto

    @Streaming
    @GET
    suspend fun downloadScheduleFile(@Url fileUrl: String): ResponseBody

    companion object{
        const val BASE_URL = "https://cloud-api.yandex.net"
        const val SCHEDULE_ACCESS_TOKEN = "AQAEA7qjvMhBAAcXnvnpRWI93E6ur3fUbjXHKtQ"
    }
}
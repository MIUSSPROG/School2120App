package com.example.school2120app.data.remote

import com.example.school2120app.data.remote.yandexCloudDto.FilesDto
import okhttp3.ResponseBody
import retrofit2.http.Header
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface YandexCloudApi {

    @GET("/v1/disk/resources/files")
    suspend fun getAllFiles(@Header("Authorization") token: String): FilesDto

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): ResponseBody

    @GET
    suspend fun getPreview(@Header("Authorization") token: String, @Url previewUrl: String): ResponseBody

    companion object{
        const val BASE_URL = "https://cloud-api.yandex.net"
        const val ACCESS_TOKEN = "AQAEA7qjvMhBAAcXnv8LaFDp2UMJhg5PkkVo7Ko"
    }
}
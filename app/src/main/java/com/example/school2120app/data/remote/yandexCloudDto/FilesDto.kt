package com.example.school2120app.data.remote.yandexCloudDto

import com.google.gson.annotations.SerializedName


data class FilesDto(
    @SerializedName("items")
    val fileItems: List<FileItemDto>,
    val limit: Int,
    val offset: Int
)
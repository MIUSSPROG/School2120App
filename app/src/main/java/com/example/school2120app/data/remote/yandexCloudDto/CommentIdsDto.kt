package com.example.school2120app.data.remote.yandexCloudDto


import com.google.gson.annotations.SerializedName

data class CommentIdsDto(
    @SerializedName("private_resource")
    val privateResource: String,
    @SerializedName("public_resource")
    val publicResource: String
)
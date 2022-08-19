package com.example.school2120app.data.remote.profile.dto

import com.google.gson.annotations.SerializedName

data class UserDocHistoryDto(
    @SerializedName("doc_id")
    val id: Int = 0,
    val date: String? = null,
    val title: String? = null
)
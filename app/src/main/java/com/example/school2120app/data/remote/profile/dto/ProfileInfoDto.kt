package com.example.school2120app.data.remote.profile.dto

import com.google.gson.annotations.SerializedName

data class ProfileInfoDto (
    @SerializedName("id_original")
    val id: String? = null,
    val surname: String? = null,
    val name: String? = null,
    val patronymic: String? = null,
    val email: String? = null,
    val place: String? = null,
    val functionality: String? = null
)
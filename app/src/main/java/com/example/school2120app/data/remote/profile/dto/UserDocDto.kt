package com.example.school2120app.data.remote.profile.dto

import com.google.gson.annotations.SerializedName

data class UserDocDto(
    @SerializedName("doc_id")
    val id: Int = 0,
    val title: String? = null,

    @SerializedName("doc_type")
    val type: String? = null,

    @SerializedName("doc_subject")
    val subject: String? = null,

    @SerializedName("doc_functionality")
    val functionality: String? = null,

    @SerializedName("doc_places")
    val places: String? = null,

    @SerializedName("date_end")
    val endDate: String? = null,

    @SerializedName("is_subscribe")
    val isSubscribe: Int,

    @SerializedName("is_unsubscribe")
    val isUnsubscribe: Int,
    val file: String? = null,

    @SerializedName("subscribe_link")
    val subLink: String? = null,

    @SerializedName("unsubscribe_link")
    val unsubLink: String? = null
)
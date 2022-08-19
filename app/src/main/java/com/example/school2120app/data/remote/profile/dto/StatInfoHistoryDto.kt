package com.example.school2120app.data.remote.profile.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class StatInfoHistoryDto (
    @SerializedName("doc_id")
    val id: Int = 0,
    val date: String? = null,
    val dateReal: Date? = null,
    val title: String? = null,

    @SerializedName("doc_type")
    val type: String? = null,

    @SerializedName("doc_subject")
    val subject: String? = null,

    @SerializedName("doc_functionality")
    val functionality: String? = null,

    @SerializedName("doc_places")
    val places: String? = null,

    @SerializedName("doc_users")
    val receivers: String? = null,

    @SerializedName("stat_is_subscribe")
    val numOfSub: String? = null,

    @SerializedName("stat_is_unsubscribe")
    val numOfUnsub: String? = null,
    val expanded: Boolean? = null
)
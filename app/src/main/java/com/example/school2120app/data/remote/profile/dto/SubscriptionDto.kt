package com.example.school2120app.data.remote.profile.dto

import com.google.gson.annotations.SerializedName

data class SubscriptionDto(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("docs_id")
    val docId: Int,
    @SerializedName("subscribe")
    val subscribed: Int? = null,
    @SerializedName("unsubscribe")
    val unsubscribed: Int? = null
)
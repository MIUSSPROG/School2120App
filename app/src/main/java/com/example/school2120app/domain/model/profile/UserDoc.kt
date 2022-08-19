package com.example.school2120app.domain.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDoc(
    val id: Int,
    val title: String,
    val type: String,
    val subject: String,
    val endDate: String,
    val isSubscribe: Int,
    val isUnsubscribe: Int,
    val fileUrl: String,
    val subLink: String,
    val unSubLink: String
): Parcelable
package com.example.school2120app.domain.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDocHistory(
    val id: Int,
    val date: String,
    val title: String
): Parcelable
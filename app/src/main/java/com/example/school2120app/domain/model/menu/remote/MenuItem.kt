package com.example.school2120app.domain.model.menu.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MenuItem(
    val name: String,
    val date: Date,
    val previewUrl: String,
    val downloadUrl: String
): Parcelable

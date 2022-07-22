package com.example.school2120app.domain.model.news

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val id: Int,
    val anons: String,
    val content: String,
    val name: String,
    val publishDate: String
): Parcelable
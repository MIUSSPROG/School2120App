package com.example.school2120app.data.local.news

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity(
    @PrimaryKey
    val id: Int,
    val anons: String,
    val content: String,
    val name: String,
    val publishDate: String
)
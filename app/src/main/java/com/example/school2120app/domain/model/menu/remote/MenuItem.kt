package com.example.school2120app.domain.model.menu.remote

import java.time.LocalDate
import java.util.*

data class MenuItem(
    val name: String,
    val date: Date,
    val previewUrl: String,
    val downloadUrl: String
)

package com.example.school2120app.data.local.menu

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: Calendar,
    val previewUrl: String,
    val downloadUrl: String
)
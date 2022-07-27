package com.example.school2120app.data.local.schedule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScheduleBuildingEntity(
    @PrimaryKey
    val building: String
)

package com.example.school2120app.data.local.schedule

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ScheduleBuildingEntity::class,
            parentColumns = ["building"],
            childColumns = ["building"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class ScheduleGradeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val grade: String,
    val letter: String,
    val building: String
)

package com.example.school2120app.data.local.schedule

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ScheduleGradeEntity::class,
            parentColumns = ["id"],
            childColumns = ["gradeId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class ScheduleLessonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val room: String,
    val gradeId: Int,
    val weekday: String
)

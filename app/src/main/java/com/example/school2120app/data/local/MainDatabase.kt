package com.example.school2120app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.school2120app.data.local.news.NewsDao
import com.example.school2120app.data.local.news.NewsEntity
import com.example.school2120app.data.local.schedule.*

@Database(
    entities = [NewsEntity::class, ScheduleBuildingEntity::class, ScheduleGradeEntity::class, ScheduleLessonEntity::class],
    version = 1
)
abstract class MainDatabase: RoomDatabase() {
    abstract val daoNews: NewsDao
    abstract val daoSchedule: ScheduleDao
}
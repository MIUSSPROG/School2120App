package com.example.school2120app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.school2120app.core.util.Converters
import com.example.school2120app.data.local.contacts.ContactDao
import com.example.school2120app.data.local.contacts.ContactInfoEntity
import com.example.school2120app.data.local.menu.MenuDao
import com.example.school2120app.data.local.menu.MenuItemEntity
import com.example.school2120app.data.local.news.NewsDao
import com.example.school2120app.data.local.news.NewsEntity
import com.example.school2120app.data.local.schedule.*

@Database(
    entities = [NewsEntity::class,
        ScheduleBuildingEntity::class,
        ScheduleGradeEntity::class,
        ScheduleLessonEntity::class,
        MenuItemEntity::class,
        ContactInfoEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {
    abstract val daoNews: NewsDao
    abstract val daoSchedule: ScheduleDao
    abstract val daoMenu: MenuDao
    abstract val daoContact: ContactDao
}
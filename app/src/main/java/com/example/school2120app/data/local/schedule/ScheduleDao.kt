package com.example.school2120app.data.local.schedule

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuilding(scheduleBuildingEntity: ScheduleBuildingEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGradeInfo(scheduleGradeEntity: ScheduleGradeEntity): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertLesson(scheduleLessonEntity: ScheduleLessonEntity)


}
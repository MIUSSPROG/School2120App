package com.example.school2120app.data.local.schedule

import androidx.room.*
import com.example.school2120app.domain.model.schedule.local.GradeLesson

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuilding(scheduleBuildingEntity: ScheduleBuildingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGradeInfo(scheduleGradeEntity: ScheduleGradeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertLesson(scheduleLessonEntity: ScheduleLessonEntity)

    @Query("""
        SELECT sl.name, sl.room, sl.weekday 
        FROM ScheduleGradeEntity as sg JOIN ScheduleLessonEntity as sl 
        ON sg.id = sl.gradeId 
        WHERE sg.grade = :grade AND sg.letter = :letter AND sg.building = :building AND sl.weekday = :weekday
    """)
    suspend fun getSchedule(building: String, grade: String, letter: String, weekday: String): List<GradeLesson>
}
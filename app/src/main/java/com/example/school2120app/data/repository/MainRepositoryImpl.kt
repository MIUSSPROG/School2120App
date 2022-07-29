package com.example.school2120app.data.repository

import android.util.Log
import com.example.school2120app.core.util.Resource
import com.example.school2120app.data.local.news.NewsDao
import com.example.school2120app.data.local.schedule.*
import com.example.school2120app.data.mapper.toItem
import com.example.school2120app.data.mapper.toNews
import com.example.school2120app.data.mapper.toNewsEntity
import com.example.school2120app.data.remote.news.NewsApi
import com.example.school2120app.data.remote.schedule.ScheduleApi
import com.example.school2120app.data.remote.schedule.ScheduleApi.Companion.SCHEDULE_ACCESS_TOKEN
import com.example.school2120app.data.xlsx.XlsxParser
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.schedule.local.GradeLesson
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.sql.SQLException

class MainRepositoryImpl(
    private val newsApi: NewsApi,
    private val scheduleApi: ScheduleApi,
    private val newsDao: NewsDao,
    private val scheduleDao: ScheduleDao,
    private val scheduleParser: XlsxParser<ScheduleByBuilding>
) : MainRepository {


    override fun getNews(
        count: Int,
        query: String?,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<News>>> = flow {
        emit(Resource.Loading())
        val localNewsList = newsDao.searchNews(query ?: "")
        val isDbEmpty = localNewsList.isEmpty()
        val loadFromCache = !isDbEmpty && !fetchFromRemote
        if (loadFromCache) {
            emit(
                Resource.Success(
                    data = localNewsList.map { it.toNews() }
                )
            )
            return@flow
        }

        val remoteNewsInfo = try {
            newsApi.getNews(count)
                .filter {
                    query?.lowercase()?.let { it1 -> it.name?.lowercase()?.startsWith(it1) } ?: true
                }
                .map { it.toNews() }

        } catch (e: HttpException) {
            emit(Resource.Error("Ошибка сервера ${e.message()}"))
            null
        } catch (e: IOException) {
            emit(Resource.Error("Ошибка чтения ${e.message}"))
            null
        }

        try {
            remoteNewsInfo?.let { newsList ->
                newsDao.clearNewsList()
                newsDao.insertNewsList(
                    newsList.map { it.toNewsEntity() }
                )
                emit(Resource.Success(
                    data = newsDao.searchNews("").map { it.toNews() }
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Ошибка базы данных"))
        }
    }

    override fun getSchedule(grade: String, letter: String, building: String, weekday: String, fetchFromRemote: Boolean): Flow<Resource<List<GradeLesson>>> = flow {
        emit(Resource.Loading())
        try {
            var localScheduleList = scheduleDao.getSchedule(building = "ш4", grade = grade, letter = letter, weekday = weekday)
            val isDbEmpty = localScheduleList.isEmpty()
            val loadFromCache = !isDbEmpty && !fetchFromRemote
            if (loadFromCache){
                emit(Resource.Success(data = localScheduleList))
                return@flow
            }

            val remoteScheduleInfo = scheduleApi.getAllFiles(SCHEDULE_ACCESS_TOKEN).scheduleItems
                .filter { it.path.split("/")[1] == "ТестРасписание" } // building вместо ТестРасписание
                .map {
                    it.toItem()
                }
                .first()

            val scheduleFileByteStream = scheduleApi.downloadScheduleFile(remoteScheduleInfo.fileUrl).byteStream()
            val remoteScheduleParsed =  scheduleParser.parse(scheduleFileByteStream)
            scheduleDao.insertBuilding(ScheduleBuildingEntity(building = "ш4"))
            val scheduleList = remoteScheduleParsed.scheduleList
            for (schedule in scheduleList){
                val gradeId = scheduleDao.insertGradeInfo(ScheduleGradeEntity(grade = schedule.grade, letter = schedule.letter, building = "ш4"))
                val lessonsByWeekday = schedule.weekdayLessons
                lessonsByWeekday?.forEach{ (weekday, lessons) ->
                    for (lesson in lessons){
                        scheduleDao.insertLesson(ScheduleLessonEntity(name = lesson.name, room = lesson.room, weekday = weekday, gradeId = gradeId.toInt()))
                    }
                }
            }
            emit(Resource.Success(data = scheduleDao.getSchedule(building = "ш4", grade = grade, letter = letter, weekday = weekday)))

        } catch (e: HttpException) {
            emit(Resource.Error("Ошибка сервера ${e.message()}"))
            Log.d("Error", e.message())
        } catch (e: IOException) {
            emit(Resource.Error("Ошибка чтения ${e.message}"))
            Log.d("Error", e.message!!)
        }catch (e: Exception){
            emit(Resource.Error("Неизвестная ошибка ${e.message}"))
            Log.d("Error", e.message!!)
            Log.d("Error", e.stackTraceToString())
        }
    }

    override fun downloadScheduleFile(fileUrl: String): Flow<Resource<Unit>> = flow {
        val fileByteStream = scheduleApi.downloadScheduleFile(fileUrl).byteStream()
        scheduleParser.parse(fileByteStream)
    }

    override fun getBuildings(): Flow<Resource<List<String>>>  = flow{
        emit(Resource.Loading())
        try {
            emit(Resource.Success(data = scheduleDao.getBuildings()))
        }catch (e: SQLException){
            emit(Resource.Error("Ошибка базы данных ${e.message}"))
        }catch (e: Exception){
            emit(Resource.Error("Неизветсная ошибка ${e.message}"))
        }
    }

    override fun getGrades(building: String): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(data = scheduleDao.getGrades(building)))
        }catch (e: SQLException){
            emit(Resource.Error("Ошибка базы данных ${e.message}"))
        }catch (e: Exception){
            emit(Resource.Error("Неизветсная ошибка ${e.message}"))
        }
    }


}
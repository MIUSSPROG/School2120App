package com.example.school2120app.data.repository

import android.util.Log
import com.example.school2120app.core.util.Resource
import com.example.school2120app.data.local.NewsDao
import com.example.school2120app.data.mapper.toItem
import com.example.school2120app.data.mapper.toNews
import com.example.school2120app.data.mapper.toNewsEntity
import com.example.school2120app.data.remote.news.NewsApi
import com.example.school2120app.data.remote.schedule.ScheduleApi
import com.example.school2120app.data.remote.schedule.ScheduleApi.Companion.SCHEDULE_ACCESS_TOKEN
import com.example.school2120app.data.xlsx.XlsxParser
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import com.example.school2120app.domain.model.schedule.remote.ScheduleItem
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class MainRepositoryImpl(
    private val newsApi: NewsApi,
    private val scheduleApi: ScheduleApi,
    private val dao: NewsDao,
    private val scheduleParser: XlsxParser<ScheduleByBuilding>
) : MainRepository {


    override fun getNews(
        count: Int,
        query: String?,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<News>>> = flow {
        emit(Resource.Loading())
        val localNewsList = dao.searchNews(query ?: "")
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
                dao.clearNewsList()
                dao.insertNewsList(
                    newsList.map { it.toNewsEntity() }
                )
                emit(Resource.Success(
                    data = dao.searchNews("").map { it.toNews() }
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Ошибка базы данных"))
        }
    }

    override fun getSchedule(building: String): Flow<Resource<ScheduleByBuilding>> = flow {
        emit(Resource.Loading())
        try {
            val scheduleInfo = scheduleApi.getAllFiles(SCHEDULE_ACCESS_TOKEN).scheduleItems
                .filter { it.path.split("/")[1] == "ТестРасписание" } // building вместо ТестРасписание
                .map {
                    it.toItem()
                }
                .first()
//            downloadScheduleFile(scheduleInfo.fileUrl)
            val fileByteStream = scheduleApi.downloadScheduleFile(scheduleInfo.fileUrl).byteStream()
            val scheduleParsed =  scheduleParser.parse(fileByteStream)
            emit(Resource.Success(scheduleParsed))

        } catch (e: HttpException) {
            emit(Resource.Error("Ошибка сервера ${e.message()}"))
            Log.d("Error", e.message())
        } catch (e: IOException) {
            emit(Resource.Error("Ошибка чтения ${e.message}"))
            Log.d("Error", e.message!!)
        }
    }

    override fun downloadScheduleFile(fileUrl: String): Flow<Resource<Unit>> = flow {
        val fileByteStream = scheduleApi.downloadScheduleFile(fileUrl).byteStream()
        scheduleParser.parse(fileByteStream)
    }


}
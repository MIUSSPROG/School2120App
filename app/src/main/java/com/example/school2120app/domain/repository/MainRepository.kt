package com.example.school2120app.domain.repository

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.schedule.remote.ScheduleItem
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getNews(count: Int, query: String?, fetchFromRemote: Boolean): Flow<Resource<List<News>>>
    fun getSchedule(): Flow<Resource<ScheduleItem>>
    fun downloadScheduleFile(fileUrl: String): Flow<Resource<Unit>>
}
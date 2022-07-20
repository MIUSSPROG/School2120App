package com.example.school2120app.domain.repository

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.news.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(count: Int): Flow<Resource<List<News>>>
    fun filterNews(query: String): Flow<Resource<List<News>>>
}
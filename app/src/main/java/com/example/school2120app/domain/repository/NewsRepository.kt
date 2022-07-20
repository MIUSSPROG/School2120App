package com.example.school2120app.domain.repository

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.news.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(count: Int, query: String?, fetchFromRemote: Boolean): Flow<Resource<List<News>>>
}
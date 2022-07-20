package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsListUsecase(private val repository: NewsRepository) {
    operator fun invoke(count: Int, query: String?, fetchFromRemote: Boolean): Flow<Resource<List<News>>>{
        return repository.getNews(count, query, fetchFromRemote)
    }
}
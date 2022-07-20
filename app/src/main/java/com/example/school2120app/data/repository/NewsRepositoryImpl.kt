package com.example.school2120app.data.repository

import com.example.school2120app.core.util.Resource
import com.example.school2120app.data.remote.news.NewsApi
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class NewsRepositoryImpl(private val api: NewsApi): NewsRepository{

    override fun getNews(count: Int, query: String): Flow<Resource<List<News>>> = flow {
        emit(Resource.Loading())
        try {
            if (query.isNotEmpty()) {
                val remoteNewsInfo = api.getNews(count)
                    .filter { it.name?.lowercase()?.startsWith(query.lowercase()) == true }
                    .map { it.toNews() }
                emit(Resource.Success(remoteNewsInfo))
            }else{
                val remoteNewsInfo = api.getNews(count).map { it.toNews() }
                emit(Resource.Success(remoteNewsInfo))
            }
        }catch (e: HttpException){
            emit(Resource.Error("Ошибка сервера ${e.message()}"))
        }catch (e: IOException){
            emit(Resource.Error("Ошибка чтения ${e.message}"))
        }
    }

}
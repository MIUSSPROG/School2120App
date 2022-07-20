package com.example.school2120app.data.repository

import com.example.school2120app.core.util.Resource
import com.example.school2120app.data.local.NewsDao
import com.example.school2120app.data.local.NewsDatabase
import com.example.school2120app.data.mapper.toNews
import com.example.school2120app.data.mapper.toNewsEntity
import com.example.school2120app.data.remote.news.NewsApi
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class NewsRepositoryImpl(private val api: NewsApi, private val dao: NewsDao) : NewsRepository {


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
            api.getNews(count)
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

}
package com.example.school2120app.data.remote.news

import com.example.school2120app.data.remote.news.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApi {

    @GET("data/getNewsFeeds/{count}.json")
    suspend fun getNews(@Path("count") count: Int): List<NewsDto>

    companion object{
        const val BASE_URL = "https://sch2120tn.mskobr.ru/"
    }
}
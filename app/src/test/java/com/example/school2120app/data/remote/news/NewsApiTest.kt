package com.example.school2120app.data.remote.news

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.common.truth.Truth.assertThat
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class NewsApiTest {

    private lateinit var newsApi: NewsApi

    @Before
    fun setUp(){
        newsApi = Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Test
    fun testNews() = runBlocking{
        val response = newsApi.getNews(20)
        assertThat(response).isNotEmpty()
    }
}
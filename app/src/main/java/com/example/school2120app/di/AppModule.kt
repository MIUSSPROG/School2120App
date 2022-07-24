package com.example.school2120app.di

import android.app.Application
import androidx.room.Room
import com.example.school2120app.data.local.NewsDatabase
import com.example.school2120app.data.remote.news.NewsApi
import com.example.school2120app.data.remote.schedule.ScheduleApi
import com.example.school2120app.data.repository.MainRepositoryImpl
import com.example.school2120app.data.xlsx.ScheduleParser
import com.example.school2120app.data.xlsx.XlsxParser
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import com.example.school2120app.domain.repository.MainRepository
import com.example.school2120app.domain.usecase.GetNewsListUsecase
import com.example.school2120app.domain.usecase.GetScheduleUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        builder.networkInterceptors().add(httpLoggingInterceptor)
        val okHttpClient = builder.build()

        return Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideScheduleApi(): ScheduleApi{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        builder.networkInterceptors().add(httpLoggingInterceptor)
        val okHttpClient = builder.build()

        return Retrofit.Builder()
            .baseUrl(ScheduleApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ScheduleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(app: Application): NewsDatabase {
        return Room.databaseBuilder(app, NewsDatabase::class.java, "newsdb.db").build()
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsApi: NewsApi, scheduleApi: ScheduleApi, db: NewsDatabase, scheduleParser: XlsxParser<ScheduleByBuilding>): MainRepository {
        return MainRepositoryImpl(newsApi, scheduleApi, db.dao, scheduleParser)
    }

    @Provides
    @Singleton
    fun provideGetNewsListUsecase(repository: MainRepository): GetNewsListUsecase {
        return GetNewsListUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideGetScheduleUsecase(repository: MainRepository): GetScheduleUsecase{
        return GetScheduleUsecase(repository)
    }

}
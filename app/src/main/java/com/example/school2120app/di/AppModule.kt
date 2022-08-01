package com.example.school2120app.di

import android.app.Application
import androidx.room.Room
import com.example.school2120app.data.local.MainDatabase
import com.example.school2120app.data.remote.news.NewsApi
import com.example.school2120app.data.remote.YandexCloudApi
import com.example.school2120app.data.repository.MainRepositoryImpl
import com.example.school2120app.data.xlsx.XlsxParser
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.domain.model.contacts.ContactsList
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import com.example.school2120app.domain.repository.MainRepository
import com.example.school2120app.domain.usecase.*
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
    fun provideScheduleApi(): YandexCloudApi {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        builder.networkInterceptors().add(httpLoggingInterceptor)
        val okHttpClient = builder.build()

        return Retrofit.Builder()
            .baseUrl(YandexCloudApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YandexCloudApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(app: Application): MainDatabase {
        return Room.databaseBuilder(app, MainDatabase::class.java, "newsdb.db").build()
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsApi: NewsApi, yandexCloudApi: YandexCloudApi, db: MainDatabase,
                              scheduleParser: XlsxParser<ScheduleByBuilding>, contactsParser: XlsxParser<ContactsList>): MainRepository {
        return MainRepositoryImpl(newsApi, yandexCloudApi, newsDao = db.daoNews,
            scheduleDao = db.daoSchedule, menuDao = db.daoMenu, scheduleParser =  scheduleParser, contactsParser = contactsParser)
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

    @Provides
    @Singleton
    fun providesScheduleBuildingsUsecase(repository: MainRepository): GetScheduleBuildingsUsecase{
        return GetScheduleBuildingsUsecase(repository)
    }

    @Provides
    @Singleton
    fun providesScheduleGradesUsecase(repository: MainRepository): GetScheduleGradesUsecase{
        return GetScheduleGradesUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideLoadScheduleUsecase(repository: MainRepository): LoadScheduleUsecase{
        return LoadScheduleUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMenuUsecase(repository: MainRepository): GetMenusUsecase{
        return GetMenusUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPreviewUsecase(repository: MainRepository): GetPreviewUsecase{
        return GetPreviewUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideGetContactsUsecase(repository: MainRepository): GetContactsUsecase{
        return GetContactsUsecase(repository)
    }
}
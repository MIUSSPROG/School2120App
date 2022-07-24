package com.example.school2120app.di

import com.example.school2120app.data.xlsx.ScheduleParser
import com.example.school2120app.data.xlsx.XlsxParser
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScheduleParser(
        scheduleParser: ScheduleParser
    ): XlsxParser<ScheduleByBuilding>

}
package com.example.school2120app.data.mapper

import com.example.school2120app.data.local.news.NewsEntity
import com.example.school2120app.data.remote.news.dto.NewsDto
import com.example.school2120app.data.remote.schedule.dto.ScheduleItemDto
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.schedule.remote.ScheduleItem

fun NewsEntity.toNews(): News{
    return News(
        id = id,
        publishDate = publishDate ?: "",
        name = name ?: "",
        anons = anons ?: "",
        content = content ?: ""
    )
}

fun NewsDto.toNews(): News{
    return News(
        id = id,
        publishDate = publishDate ?: "",
        name = name ?: "",
        anons = anons ?: "",
        content = content ?: ""
    )
}

fun News.toNewsEntity(): NewsEntity {
    return NewsEntity(
        id = id,
        anons = anons,
        content = content,
        name = name,
        publishDate = publishDate
    )
}

fun ScheduleItemDto.toItem(): ScheduleItem {
    return ScheduleItem(
        name = name,
        fileUrl = file
    )
}
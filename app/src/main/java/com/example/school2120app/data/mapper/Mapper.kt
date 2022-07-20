package com.example.school2120app.data.mapper

import com.example.school2120app.data.local.NewsEntity
import com.example.school2120app.data.remote.news.dto.NewsDto
import com.example.school2120app.domain.model.news.News

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

fun News.toNewsEntity(): NewsEntity{
    return NewsEntity(
        id = id,
        anons = anons,
        content = content,
        name = name,
        publishDate = publishDate
    )
}
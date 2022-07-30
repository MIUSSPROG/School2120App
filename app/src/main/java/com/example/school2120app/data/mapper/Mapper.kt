package com.example.school2120app.data.mapper

import com.example.school2120app.data.local.news.NewsEntity
import com.example.school2120app.data.remote.news.dto.NewsDto
import com.example.school2120app.data.remote.yandexCloudDto.FileItemDto
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.schedule.remote.ScheduleItem
import java.text.SimpleDateFormat

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

fun FileItemDto.toScheduleItem(): ScheduleItem {
    return ScheduleItem(
        name = name,
        fileUrl = file
    )
}

fun FileItemDto.toMenuItem(): MenuItem{

    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val menuDate = sdf.parse(name.split(' ').last().replace('.','-'))
    return MenuItem(
        name = name,
        previewUrl = preview,
        downloadUrl = file,
        date = menuDate
    )
}
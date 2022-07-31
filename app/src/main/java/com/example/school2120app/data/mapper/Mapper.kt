package com.example.school2120app.data.mapper

import com.example.school2120app.data.local.menu.MenuItemEntity
import com.example.school2120app.data.local.news.NewsEntity
import com.example.school2120app.data.remote.news.dto.NewsDto
import com.example.school2120app.data.remote.yandexCloudDto.FileItemDto
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.schedule.remote.ScheduleItem
import java.text.SimpleDateFormat
import java.util.*

fun NewsEntity.toNews(): News{
    return News(
        id = id,
        publishDate = publishDate ?: "",
        name = name ?: "",
        anons = anons ?: "",
        content = content ?: ""
    )
}

fun MenuItemEntity.toMenuItem(): MenuItem{
    return MenuItem(
        name = name,
        date = date.time,
        previewUrl = previewUrl,
        downloadUrl = downloadUrl
    )
}

fun MenuItem.toToMenuItemEntity(): MenuItemEntity{
    return MenuItemEntity(
        name = name,
        date = Calendar.getInstance().apply { time = date },
        previewUrl = previewUrl,
        downloadUrl = downloadUrl
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
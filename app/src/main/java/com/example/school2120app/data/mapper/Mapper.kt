package com.example.school2120app.data.mapper

import com.example.school2120app.data.local.contacts.ContactInfoEntity
import com.example.school2120app.data.local.menu.MenuItemEntity
import com.example.school2120app.data.local.news.NewsEntity
import com.example.school2120app.data.remote.news.dto.NewsDto
import com.example.school2120app.data.remote.profile.dto.ProfileInfoDto
import com.example.school2120app.data.remote.profile.dto.UserDocDto
import com.example.school2120app.data.remote.profile.dto.UserDocHistoryDto
import com.example.school2120app.data.remote.yandexCloudDto.FileItemDto
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.domain.model.contacts.ContactItem
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.profile.ProfileInfo
import com.example.school2120app.domain.model.profile.UserDoc
import com.example.school2120app.domain.model.profile.UserDocHistory
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

fun ContactInfoEntity.toContactInfo(): ContactInfo{
    return ContactInfo(
        position = position,
        name = name,
        address = address,
        lat = lat,
        lon = lon,
        buildingType = buildingType,
        phone = phone,
        email = email,
        photoUrl = photoUrl
    )
}

fun ContactInfo.toContactInfoEntity(): ContactInfoEntity{
    return ContactInfoEntity(
        position = position,
        name = name,
        address = address,
        lat = lat,
        lon = lon,
        buildingType = buildingType,
        phone = phone,
        email = email,
        photoUrl = photoUrl
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


fun ProfileInfoDto.toProfileInfo(): ProfileInfo{
    return ProfileInfo(
        id = id ?: -1,
        surname = surname ?: "",
        name = name ?: "",
        patronymic = patronymic ?: "",
        email = email ?: "",
        place = place ?: "",
        functionality = functionality ?: "",
        docs = docs.map { mapOf(it.keys.first() to it.values.first().toUserDoc()) },
        docsHistory = docsHistory.map { mapOf(it.keys.first() to it.values.first().toUserDocHistory()) },
        error = error ?: ""
    )
}

fun UserDocHistoryDto.toUserDocHistory(): UserDocHistory{
    return UserDocHistory(
        id = id,
        date = date ?: "",
        title = title ?: ""
    )
}

fun UserDocDto.toUserDoc(): UserDoc{
    return UserDoc(
        id = id,
        title = title ?: "Неизвестный документ",
        type = type ?: "Неизветсный тип документа",
        subject = subject ?: "Неизветсная тема документа",
        endDate = endDate ?: "Дата окончания не указана",
        isSubscribe = isSubscribe,
        isUnsubscribe = isUnsubscribe,
        fileUrl = file ?: "",
        subLink = subLink ?: "",
        unSubLink = unsubLink ?: ""
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

fun FileItemDto.toContactInfo(): ContactItem{
    return ContactItem(
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
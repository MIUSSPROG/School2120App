package com.example.school2120app.domain.repository

import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.profile.ProfileInfo
import com.example.school2120app.domain.model.schedule.local.GradeLesson
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getNews(count: Int, query: String?, fetchFromRemote: Boolean): Flow<Resource<List<News>>>
    fun getSchedule(grade: String, letter: String, building: String, weekday: String, fetchFromRemote: Boolean): Flow<Resource<List<GradeLesson>>>
    fun loadSchedule(): Flow<Resource<Unit>>
    fun getBuildings(): Flow<Resource<List<String>>>
    fun getGrades(building: String): Flow<Resource<List<String>>>
    fun getMenus(fetchFromRemote: Boolean): Flow<Resource<List<MenuItem>>>
    fun getPreview(previewUrl: String): Flow<Resource<ImageSource>>
    fun getContacts(fetchFromRemote: Boolean): Flow<Resource<List<ContactInfo>>>
    fun signIn(login: String, password: String): Flow<Resource<ProfileInfo>>
}
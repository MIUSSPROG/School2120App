package com.example.school2120app.data.repository

import android.database.sqlite.SQLiteException
import android.graphics.BitmapFactory
import android.util.Log
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.school2120app.BuildConfig.YANDEX_CLOUD_ACCESS_TOKEN
import com.example.school2120app.core.util.Resource
import com.example.school2120app.core.util.Resource.*
import com.example.school2120app.core.util.SIGNED_DOC
import com.example.school2120app.core.util.UNSIGNED_DOC
import com.example.school2120app.data.local.contacts.ContactDao
import com.example.school2120app.data.local.menu.MenuDao
import com.example.school2120app.data.local.news.NewsDao
import com.example.school2120app.data.local.schedule.*
import com.example.school2120app.data.mapper.*
import com.example.school2120app.data.remote.news.NewsApi
import com.example.school2120app.data.remote.YandexCloudApi
import com.example.school2120app.data.remote.profile.ProfileApi
import com.example.school2120app.data.xlsx.XlsxParser
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.domain.model.contacts.ContactsList
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.profile.ProfileDocs
import com.example.school2120app.domain.model.profile.ProfileInfo
import com.example.school2120app.domain.model.profile.UserDoc
import com.example.school2120app.domain.model.schedule.local.GradeLesson
import com.example.school2120app.domain.model.schedule.local.ScheduleByBuilding
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.sql.SQLException

class MainRepositoryImpl(
    private val profileApi: ProfileApi,
    private val newsApi: NewsApi,
    private val yandexCloudApi: YandexCloudApi,
    private val newsDao: NewsDao,
    private val scheduleDao: ScheduleDao,
    private val menuDao: MenuDao,
    private val contactDao: ContactDao,
    private val scheduleParser: XlsxParser<ScheduleByBuilding>,
    private val contactsParser: XlsxParser<ContactsList>,
) : MainRepository {


    override fun getNews(
        count: Int,
        query: String?,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<News>>> = flow {
        emit(Loading())
        val localNewsList = newsDao.searchNews(query ?: "")
        val isDbEmpty = localNewsList.isEmpty()
        val loadFromCache = !isDbEmpty && !fetchFromRemote
        if (loadFromCache) {
            emit(Success(data = localNewsList.map { it.toNews() }))
            return@flow
        }

        val remoteNewsInfo = try {
            newsApi.getNews(count)
                .filter {
                    query?.lowercase()?.let { it1 -> it.name?.lowercase()?.startsWith(it1) } ?: true
                }
                .map { it.toNews() }

        } catch (e: HttpException) {
            emit(Error("Ошибка сервера"))
            Log.d("Error", e.message.toString())
            null
        } catch (e: IOException) {
            emit(Error("Отсутствует интернет соединение"))
            Log.d("Error", e.message.toString())
            null
        }

        try {
            remoteNewsInfo?.let { newsList ->
                newsDao.clearNewsList()
                newsDao.insertNewsList(
                    newsList.map { it.toNewsEntity() }
                )
                emit(Success(
                    data = newsDao.searchNews("").map { it.toNews() }
                ))
            }
        } catch (e: Exception) {
            emit(Error("Ошибка базы данных"))
        }
    }

    override fun getMenus(fetchFromRemote: Boolean): Flow<Resource<List<MenuItem>>> = flow {
        emit(Loading())
        try {
            val localMenuList = menuDao.getMenuList()
            val isDbEmpty = localMenuList.isEmpty()
            val loadFromCache = !isDbEmpty && !fetchFromRemote
            if (loadFromCache){
                emit(Success(data = localMenuList.map { it.toMenuItem() }))
                return@flow
            }

            val remoteMenus = yandexCloudApi.getAllFiles(YANDEX_CLOUD_ACCESS_TOKEN).fileItems
                .filter { it.path.split("/")[1] == "Меню" }
                .map { it.toMenuItem() }

            menuDao.clearMenuList()
            menuDao.insertMenuList(remoteMenus.map { it.toToMenuItemEntity() })
            emit(Success(data = menuDao.getMenuList().map { it.toMenuItem() }.sortedByDescending { it.date }))
        }catch (e: HttpException) {
            emit(Error("Ошибка сервера"))
            Log.d("Error", e.message())
        } catch (e: IOException) {
            emit(Error("Отсутствует интернет соединение"))
            Log.d("Error", e.message!!)
        }
        catch (e: SQLiteException) {
            emit(Error("Ошибка базы данных"))
            Log.d("Error", e.message!!)
        }
    }

    override fun getContacts(fetchFromRemote: Boolean): Flow<Resource<List<ContactInfo>>> = flow {
        emit(Loading())
        try {
            val localContactList = contactDao.getContactList()
            val isDbEmpty = localContactList.isEmpty()
            val loadFromCache = !isDbEmpty && !fetchFromRemote
            if (loadFromCache){
                emit(Success(data = localContactList.map { it.toContactInfo() }))
                return@flow
            }

            val remoteContactsInfo = yandexCloudApi.getAllFiles(YANDEX_CLOUD_ACCESS_TOKEN).fileItems
                .filter { it.path.split("/")[1] == "Контакты" } // building вместо ТестРасписание
                .map { it.toContactInfo() }.first()
            val contactsFileByteStream = yandexCloudApi.downloadFile(remoteContactsInfo.fileUrl).byteStream()
            val remoteContactsParsed =  contactsParser.parse(contactsFileByteStream)

            contactDao.clearContactList()
            contactDao.insertContactList(remoteContactsParsed.contacts.map { it.toContactInfoEntity() })

            emit(Success(data = contactDao.getContactList().map { it.toContactInfo() }))

        } catch (e: HttpException) {
            emit(Error("Ошибка сервера"))
            Log.d("Error", e.message())
        } catch (e: IOException) {
            emit(Error("Отсутсвует интернет соединение"))
            Log.d("Error", e.message!!)
        } catch (e: SQLiteException) {
            emit(Error("Ошибка базы данных"))
            Log.d("Error", e.message!!)
        }
    }

    override fun signIn(login: String, password: String): Flow<Resource<ProfileDocs>> = flow {
        emit(Loading())
        try {
            val profileInfo = profileApi.login(login, password).toProfileInfo()
            val sortedDocs = sortDocuments(profileInfo = profileInfo)
            emit(Success(data = sortedDocs))
        }catch (e: HttpException) {
            emit(Error("Ошибка сервера"))
            Log.d("Error", e.message())
        } catch (e: IOException) {
            emit(Error("Отсутсвует интернет соединение"))
            Log.d("Error", e.message!!)
        }
        catch (e: Exception){
            emit(Error("Неизвестная ошибка"))
            Log.d("Error", e.message!!)
            Log.d("Error", e.stackTraceToString())
        }
    }

    private fun sortDocuments(profileInfo: ProfileInfo): ProfileDocs{
        val subscribedDocs = mutableListOf<UserDoc>()
        val unsubscribedDocs = mutableListOf<UserDoc>()
        val profileDocs = profileInfo.toProfileDocs()
        loop@ for (doc in profileInfo.docs){
            val curDoc = doc.values.first()
            for (docHistory in profileInfo.docsHistory){
                val curDocHistory = docHistory.values.first()
                if (curDoc.id == curDocHistory.id){
                    when(curDocHistory.title){
                        SIGNED_DOC -> {
                            subscribedDocs.add(curDoc)
                            continue@loop
                        }
                        UNSIGNED_DOC -> {
                            unsubscribedDocs.add(curDoc)
                            continue@loop
                        }
                    }

                }
            }
            unsubscribedDocs.add(curDoc)
        }
        profileDocs.subscribedDocs.addAll(subscribedDocs)
        profileDocs.unsubscribedDocs.addAll(unsubscribedDocs)
        return profileDocs
    }

    override fun getSchedule(grade: String, letter: String, building: String, weekday: String, fetchFromRemote: Boolean): Flow<Resource<List<GradeLesson>>> = flow {
        emit(Loading())
        try {
            var localScheduleList = scheduleDao.getSchedule(building = "ш4", grade = grade, letter = letter, weekday = weekday)
            val isDbEmpty = localScheduleList.isEmpty()
            val loadFromCache = !isDbEmpty && !fetchFromRemote
            if (loadFromCache){
                emit(Success(data = localScheduleList))
                return@flow
            }

            emit(Success(data = scheduleDao.getSchedule(building = "ш4", grade = grade, letter = letter, weekday = weekday)))

        } catch (e: SQLiteException) {
            emit(Error("Ошибка базы данных"))
            Log.d("Error", e.message!!)
        } catch (e: Exception){
            emit(Error("Неизвестная ошибка"))
            Log.d("Error", e.message!!)
            Log.d("Error", e.stackTraceToString())
        }
    }

    override fun getPreview(previewUrl: String): Flow<Resource<ImageSource>> = flow {
        emit(Loading())
        try {
            val url = previewUrl.replace("size=S", "size=XXXL")
            val response =
                yandexCloudApi.getPreview(token = "OAuth $YANDEX_CLOUD_ACCESS_TOKEN", previewUrl = url)
            val bmp = BitmapFactory.decodeStream(response.byteStream())
            val imageSource = ImageSource.bitmap(bmp)
            emit(Success(data = imageSource))
        } catch (e: HttpException) {
            emit(Error("Ошибка сервера"))
            Log.d("Error", e.message())
        } catch (e: IOException) {
            emit(Error("Отсутсвует интернет соединение"))
            Log.d("Error", e.message!!)
        }
    }

    override fun loadSchedule(): Flow<Resource<Unit>> = flow {
        emit(Loading())
        try {
            val remoteScheduleInfo = yandexCloudApi.getAllFiles(YANDEX_CLOUD_ACCESS_TOKEN).fileItems
                .filter { it.path.split("/")[1] == "ТестРасписание" }
                .map { it.toScheduleItem() }.first()

            val scheduleFileByteStream = yandexCloudApi.downloadFile(remoteScheduleInfo.fileUrl).byteStream()
            val remoteScheduleParsed =  scheduleParser.parse(scheduleFileByteStream)
            scheduleDao.insertBuilding(ScheduleBuildingEntity(building = "ш4"))
            val scheduleList = remoteScheduleParsed.scheduleList
            for (schedule in scheduleList){
                val gradeId = scheduleDao.insertGradeInfo(ScheduleGradeEntity(grade = schedule.grade, letter = schedule.letter, building = "ш4"))
                val lessonsByWeekday = schedule.weekdayLessons
                lessonsByWeekday?.forEach{ (weekday, lessons) ->
                    for (lesson in lessons){
                        scheduleDao.insertLesson(ScheduleLessonEntity(name = lesson.name, room = lesson.room, weekday = weekday, gradeId = gradeId.toInt()))
                    }
                }
            }
            emit(Success(data = null))
        }catch (e: HttpException) {
            emit(Error("Ошибка сервера"))
            Log.d("Error", e.message())
        } catch (e: IOException) {
            emit(Error("Отсутствует интернет соединение"))
            Log.d("Error", e.message!!)
        }catch (e: Exception){
            emit(Error("Неизвестная ошибка"))
            Log.d("Error", e.message!!)
            Log.d("Error", e.stackTraceToString())
        }
    }

    override fun getBuildings(): Flow<Resource<List<String>>>  = flow{
        emit(Loading())
        try {
            emit(Success(data = scheduleDao.getBuildings()))
        }catch (e: SQLException){
            Log.d("Error", e.message.toString())
            emit(Error("Ошибка базы данных"))
        }catch (e: Exception){
            Log.d("Error", e.message.toString())
            emit(Error("Неизветсная ошибка"))
        }
    }

    override fun getGrades(building: String): Flow<Resource<List<String>>> = flow {
        emit(Loading())
        try {
            emit(Success(data = scheduleDao.getGrades(building)))
        }catch (e: SQLException){
            emit(Error("Ошибка базы данных"))
            Log.d("Error", e.message.toString())
        }catch (e: Exception){
            emit(Error("Неизветсная ошибка"))
            Log.d("Error", e.message.toString())
        }
    }


}
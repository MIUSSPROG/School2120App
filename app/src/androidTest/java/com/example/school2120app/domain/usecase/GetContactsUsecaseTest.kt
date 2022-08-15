package com.example.school2120app.domain.usecase

import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.example.school2120app.domain.model.news.News
import com.example.school2120app.domain.model.schedule.local.GradeLesson
import com.example.school2120app.domain.repository.MainRepository
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mockito.Mockito
import org.mockito.kotlin.mock


//class TestRepository: MainRepository{
//    override fun getNews(
//        count: Int,
//        query: String?,
//        fetchFromRemote: Boolean
//    ): Flow<Resource<List<News>>> {
//        return flow { Resource.Success(data = emptyList<News>()) }
//    }
//
//    override fun getSchedule(
//        grade: String,
//        letter: String,
//        building: String,
//        weekday: String,
//        fetchFromRemote: Boolean
//    ): Flow<Resource<List<GradeLesson>>> {
//        return flow {  }
//    }
//
//    override fun loadSchedule(): Flow<Resource<Unit>> {
//        return flow {  }
//    }
//
//    override fun getBuildings(): Flow<Resource<List<String>>> {
//        return flow {  }
//    }
//
//    override fun getGrades(building: String): Flow<Resource<List<String>>> {
//        return flow {  }
//    }
//
//    override fun getMenus(fetchFromRemote: Boolean): Flow<Resource<List<MenuItem>>> {
//        return flow {  }
//    }
//
//    override fun getPreview(previewUrl: String): Flow<Resource<ImageSource>> {
//        return flow {  }
//    }
//
//    override fun getContacts(fetchFromRemote: Boolean): Flow<Resource<List<ContactInfo>>> {
//        return flow {  }
//    }
//}

class GetContactsUsecaseTest {

    val testRepository = mock<MainRepository>()

    @Test
    fun emptyListShouldBeGotten() {

        Mockito.`when`(testRepository.getNews(fetchFromRemote = true, query = null, count = 10))
            .thenReturn(flow { Resource.Success(data = emptyList<News>()) })

        val useCase = GetContactsUsecase(repository = testRepository)
        val actual = useCase(fetchFromRemote = false)
        val expected = Resource.Success(data = emptyList<News>())
        assertThat(actual).isEqualTo(expected)
    }
}
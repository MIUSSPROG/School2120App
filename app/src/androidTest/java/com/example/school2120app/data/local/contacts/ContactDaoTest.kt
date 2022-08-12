package com.example.school2120app.data.local.contacts

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.school2120app.data.local.MainDatabase
//import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

//@ExperimentalCoroutinesApi
//@SmallTest
//@HiltAndroidTest
class ContactDaoTest: TestCase() {

//    @Rule
//    var instantTaskExecutorRule = TaskExecutor()

//    @Rule
//    var hiltRule = HiltAndroidRule(this)

//    @Inject
//    @Named("test_db")
    private lateinit var database: MainDatabase
    private lateinit var contactDao: ContactDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MainDatabase::class.java
        ).allowMainThreadQueries().build()
        contactDao = database.daoContact
//        hiltRule.inject()
//        contactDao = database.daoContact
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertContactList() = runBlocking {
        val contactInfo = ContactInfoEntity(
            id = 1,
            position = "testPosition",
            name = "testName",
            address = "testAddress",
            lat = 1.1,
            lon = 2.2,
            buildingType = "Школа",
            phone = "+7-926-709-07-29",
            email = "test@mail.ru",
            photoUrl = "testPhotoUrl"
        )
        contactDao.insertContactList(listOf(contactInfo))

        val allContacts = contactDao.getContactList()

        assertThat(allContacts).contains(contactInfo)
    }

    @Test
    fun clearContactList() = runBlocking {
        val contactItem1 = ContactInfoEntity(
            id = 1,
            position = "testPosition",
            name = "testName",
            address = "testAddress",
            lat = 1.1,
            lon = 2.2,
            buildingType = "Школа",
            phone = "+7-926-709-07-29",
            email = "test@mail.ru",
            photoUrl = "testPhotoUrl"
        )
        val contactItem2 = ContactInfoEntity(
            id = 2,
            position = "testPosition2",
            name = "testName2",
            address = "testAddress2",
            lat = 1.1,
            lon = 2.2,
            buildingType = "Школа2",
            phone = "+7-926-709-07-29",
            email = "test2@mail.ru",
            photoUrl = "testPhotoUrl2"
        )
        contactDao.insertContactList(listOf(contactItem1, contactItem2))
        contactDao.clearContactList()
        val allContacts = contactDao.getContactList()
        assertThat(allContacts).isEmpty()
    }
}
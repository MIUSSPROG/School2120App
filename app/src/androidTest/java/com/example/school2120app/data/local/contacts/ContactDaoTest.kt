package com.example.school2120app.data.local.contacts

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.school2120app.data.local.MainDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat

@RunWith(AndroidJUnit4::class)
class ContactDaoTest{

    private lateinit var db: MainDatabase
    private lateinit var dao: ContactDao

    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MainDatabase::class.java).build()
        dao = db.daoContact
    }

    @After
    fun teardown(){
        db.close()
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
        dao.insertContactList(listOf(contactInfo))

        val allContacts = dao.getContactList()

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
        dao.insertContactList(listOf(contactItem1, contactItem2))
        dao.clearContactList()
        val allContacts = dao.getContactList()
        assertThat(allContacts).isEmpty()
    }
}
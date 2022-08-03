package com.example.school2120app.data.local.contacts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.school2120app.data.local.menu.MenuItemEntity

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactList(contactList: List<ContactInfoEntity>)

    @Query("DELETE FROM ContactInfoEntity")
    suspend fun clearContactList()

    @Query("SELECT * FROM ContactInfoEntity")
    suspend fun getContactList(): List<ContactInfoEntity>
}
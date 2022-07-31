package com.example.school2120app.data.local.menu

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MenuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuList(menuList: List<MenuItemEntity>)

    @Query("DELETE FROM MenuItemEntity")
    suspend fun clearMenuList()

    @Query("SELECT * FROM MenuItemEntity ORDER BY date DESC")
    suspend fun getMenuList(): List<MenuItemEntity>
}
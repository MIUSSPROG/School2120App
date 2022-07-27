package com.example.school2120app.data.local.news

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsList(newsListEntities: List<NewsEntity>)

    @Query("DELETE FROM newsentity")
    suspend fun clearNewsList()

    @Query("""
        SELECT * 
        FROM newsentity
        WHERE LOWER(name) LIKE LOWER(:query) || '%'
        ORDER BY id DESC
    """)
    suspend fun searchNews(query: String): List<NewsEntity>
}
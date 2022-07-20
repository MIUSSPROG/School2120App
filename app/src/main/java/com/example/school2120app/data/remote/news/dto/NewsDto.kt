package com.example.school2120app.data.remote.news.dto


import com.example.school2120app.domain.model.news.News
import com.google.gson.annotations.SerializedName

data class NewsDto(
    val anons: String? = null,
    val content: String? = null,
    val id: Int,
    val link: String? = null,
    val name: String? = null,
    @SerializedName("publish_date")
    val publishDate: String? = null,
    val tags: List<Int>? = null
){
    fun toNews(): News{
        return News(
            id = id,
            publishDate = publishDate ?: "",
            name = name ?: "",
            anons = anons ?: "",
            content = content ?: ""
        )
    }
}
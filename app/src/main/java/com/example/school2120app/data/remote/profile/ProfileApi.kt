package com.example.school2120app.data.remote.profile

import com.example.school2120app.data.remote.profile.dto.ProfileInfoDto
import com.example.school2120app.data.remote.profile.dto.StatInfoDto
import okhttp3.ResponseBody
import retrofit2.http.*

interface ProfileApi {

    @FormUrlEncoded
    @POST("user.php")
    suspend fun login(
        @Field("user") email: String,
        @Field("passwd") passwd: String
    ): ProfileInfoDto

    @GET("docs_stat.php")
    suspend fun getUsersStat(): StatInfoDto

    @GET
    suspend fun subscribeDocument(@Url url: String): ResponseBody

    companion object{
        const val BASE_URL = "http://intranet.sch2120-do.ru/api/"
    }
}
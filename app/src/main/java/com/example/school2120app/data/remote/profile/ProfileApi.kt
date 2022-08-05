package com.example.school2120app.data.remote.profile

import com.example.school2120app.data.remote.profile.dto.ProfileInfoDto
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProfileApi {

    @FormUrlEncoded
    @POST("user.php")
    fun login(
        @Field("user") email: String?,
        @Field("passwd") passwd: String?
    ): ProfileInfoDto

    companion object{
        const val BASE_URL = "http://intranet.sch2120-do.ru/api/"
    }
}
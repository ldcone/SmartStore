package com.example.smartstore.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("user")
    fun registUser():Boolean

    @GET("user/info")
    fun selectUserInfo(id:String)

    @GET("user/isUsed")
    suspend fun checkIsUsed(@Query("id") id:String): Response<Boolean>
}
package com.example.smartstore.api

import android.media.session.MediaSession
import com.ssafy.smartstore.dto.Token
import com.ssafy.smartstore.dto.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface FirebaseTokenService {
    //Token정보 서버로 전송
    @POST("rest/token")
    suspend fun uploadToken(@Body body:Token ): Boolean

    @POST("rest/token/update")
    suspend fun updateToken(@Body body:Token ): Boolean
}
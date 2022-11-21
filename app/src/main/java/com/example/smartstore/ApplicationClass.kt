package com.example.smartstore

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationClass: Application() {
    private val SERVER_URL = "https://3.35.218.206:8080/rest/"

    companion object {
        // 전역변수 문법을 통해 Retrofit 인스턴스를 앱 실행 시 1번만 생성하여 사용 (싱글톤 객체)
        lateinit var retrofit: Retrofit
//        var userInfo:UserDTO? = null

        // 사용자 정보 가져오기
//        fun getUserInfo(callback: Callback<UserDTO>){
//            val service = retrofit.create(UserService::class.java)
//            service.selectUser().enqueue(callback)
//        }

        // 사용자 정보 업데이트
//        fun updateUserInfo(_userInfo:UserDTO, callback: Callback<Unit>){
//            userInfo = _userInfo
//            val service = retrofit.create(UserService::class.java)
//            service.updateUser(userInfo!!).enqueue(callback)
//        }
    }

    override fun onCreate() {
        super.onCreate()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
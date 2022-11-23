package com.example.smartstore.response

import com.ssafy.smartstore.dto.Stamp

data class UserResponse(
    val id:Int,
    val userId: String,
    val name: String,
    val pass: String,
    val stamps: Int,
    val stampList: ArrayList<Stamp> = ArrayList()
)
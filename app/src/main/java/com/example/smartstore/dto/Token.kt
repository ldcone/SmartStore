package com.ssafy.smartstore.dto

data class Token (
    val id:Int,
    val userId: String,
    val token: String,
){
    constructor():this(0,"", "")
}
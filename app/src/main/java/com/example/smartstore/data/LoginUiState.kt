package com.example.smartstore.data

/**
 * id : 아이디
 * password : 비밀번호
 * nickname : 닉네임
 * isChecked : 아이디 중복체크 여부
 * */

data class LoginUiState(
    var id:String,
    var password:String,
    var nickname:String,
    var isChecked:Boolean
)
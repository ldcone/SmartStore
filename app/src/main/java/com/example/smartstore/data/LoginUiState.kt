package com.example.smartstore.data

/**
 * id : 아이디
 * password : 비밀번호
 * nickname : 닉네임
 * isChecked : 아이디 중복체크 여부
 * canRegist : 0 -> 아이디 없음, 1 -> 중복체크 안함, 2 -> 비밀번호 없음, 3 -> 닉네임 없음, 4 -> 정상
 * */

data class LoginUiState(
    var id:String,
    var password:String,
    var nickname:String,
    var isChecked:Boolean
)
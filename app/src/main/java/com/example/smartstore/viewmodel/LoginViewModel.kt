package com.example.smartstore.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.smartstore.data.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val TAG = "LoginViewModel_싸피"
class LoginViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState("","", "", false))
    val uiState = _uiState.asStateFlow()

    fun setLoginInfo(id:String, nickname:String, password:String, isChecked: Boolean){
        _uiState.update { currentState ->
            currentState.copy(
                id = id,
                nickname = nickname,
                password = password,
                isChecked = isChecked
            )
        }
    }

    // 아이디 중복체크
    fun checkDuplicate(id:String){
        val _id = "test"

        Log.d(TAG, "id : $id, 결과 : ${_id == id}")

        if(_id == id || id.isEmpty()){
            _uiState.update{ currentState ->
                currentState.copy(
                    isChecked = false
                )
            }
        }else{
            _uiState.update{ currentState ->
                currentState.copy(
                    isChecked = true
                )
            }
        }
        Log.d(TAG, "checkDuplicate: ${uiState.value.isChecked}")
    }

    // 회원가입 데이터 형식 확인
    fun checkJoinData(loginInfo:LoginUiState):Int{
        if(loginInfo.id.isEmpty()){
            return 0
        }else if(!_uiState.value.isChecked){
            return 1
        }else if(loginInfo.password.isEmpty()){
            return 2
        }else if(loginInfo.nickname.isEmpty()){
            return 3
        }else{
            return 4
        }
    }

    // viewModel 값 초기화
    fun resetState(){
        setLoginInfo("","","",false)
    }

    // 로그인 확인
    fun checkLogin(loginInfo: LoginUiState):Boolean{
        if(loginInfo.id == "test" && loginInfo.password == "1234"){
            return true
        }else{
            return false
        }
    }
}
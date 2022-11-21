package com.example.smartstore.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstore.ApplicationClass
import com.example.smartstore.data.LoginUiState
import com.example.smartstore.service.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface LoginRestState {
    data class Success(val result:Boolean) : LoginRestState
    object Loading : LoginRestState
    object Error : LoginRestState
}

private const val TAG = "LoginViewModel_싸피"
class LoginViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState("","", "", false))
    val uiState = _uiState.asStateFlow()
    var loginState:LoginRestState by mutableStateOf(LoginRestState.Loading)
        private set

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
    suspend fun checkDuplicate(id:String){
        viewModelScope.launch {
            try{
                val service = ApplicationClass.retrofit.create(UserService::class.java)
                val result = service.checkIsUsed(id)

                Log.d(TAG, "checkDuplicate response: ${result.body()}")

                if(result.isSuccessful){
                    _uiState.update{ currentState ->
                        currentState.copy(
                            isChecked = result.body()!!
                        )
                    }
                }else{
                    Log.d(TAG, "checkDuplicate: 실패")
                }
            }catch(e:IOException){
                loginState = LoginRestState.Error
            }
        }.join()

//        if(){
//            _uiState.update{ currentState ->
//                currentState.copy(
//                    isChecked = false
//                )
//            }
//        }else{
//            _uiState.update{ currentState ->
//                currentState.copy(
//                    isChecked = true
//                )
//            }
//        }
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
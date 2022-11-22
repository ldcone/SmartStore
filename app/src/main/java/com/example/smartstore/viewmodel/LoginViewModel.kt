package com.example.smartstore.viewmodel

import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstore.ApplicationClass
import com.example.smartstore.data.LoginUiState
import com.example.smartstore.service.UserService
import com.ssafy.smartstore.api.UserApi
import com.ssafy.smartstore.dto.User
import com.ssafy.smartstore.util.RetrofitUtil
import com.ssafy.smartstore.util.SharedPreferencesUtil
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

    private val _isCheckd = MutableLiveData(false)
    val checkState: LiveData<Boolean> = _isCheckd

    fun onChangeChecked(isDuplicated:Boolean){
        _isCheckd.value = isDuplicated
    }


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
    suspend fun checkDuplicate(id:String):Boolean{
        var isDuplicated = false
        viewModelScope.launch {
            try{
                val result = RetrofitUtil.userService.isUsedId(id)

                Log.d(TAG, "checkDuplicate response: ${result.body()}")

                if(result.isSuccessful){
                    isDuplicated = result.body()!!
                    onChangeChecked(!isDuplicated)
                }else{
                    Log.d(TAG, "checkDuplicate: 실패")
                }
            }catch(e:IOException){
                Log.d(TAG, "checkDuplicate: ${e.message}")
                loginState = LoginRestState.Error
            }
        }.join()

        Log.d(TAG, "checkDuplicate: ${uiState.value.isChecked}")
        return isDuplicated
    }

    // 회원가입 데이터 형식 확인
    fun checkJoinData(loginInfo:LoginUiState):Int{
        if(loginInfo.id.isEmpty()){
            return 0
        }else if(!loginInfo.isChecked){
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
        onChangeChecked(false)
    }

    // 회원 가입
    suspend fun registUser(loginInfo:LoginUiState):Boolean{
        val user = User(loginInfo.id, loginInfo.nickname, loginInfo.password, 0)
        var isSuccess = false
        viewModelScope.launch {
            try{
                val result = RetrofitUtil.userService.insert(user)

                Log.d(TAG, "checkDuplicate response: ${result.body()}")

                if(result.isSuccessful){
                    isSuccess = result.body()!!
                }else{
                    Log.d(TAG, "registUser : 실패 ${result.message()}")
                }
            }catch(e:IOException){
                Log.d(TAG, "checkDuplicate: ${e.message}")
                loginState = LoginRestState.Error
            }
        }.join()

        return isSuccess
    }

    // 로그인 확인
    suspend fun checkLogin(loginInfo: LoginUiState):User?{
        val user = User(loginInfo.id, loginInfo.nickname, loginInfo.password, 0)
        var loginUser:User? = null
        viewModelScope.launch {
            try{
                var result = RetrofitUtil.userService.login(user)

                if(result.isSuccessful){
                   loginUser = result.body() as User
                }
            }catch(e:IOException){
                Log.d(TAG, "checkLogin: ${e.message}")
                loginState = LoginRestState.Error
            }
        }.join()
        return loginUser
    }
}
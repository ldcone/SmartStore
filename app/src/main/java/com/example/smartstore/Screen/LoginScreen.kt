package com.example.smartstore.Screen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartstore.ApplicationClass
import com.example.smartstore.LoginActivity
import com.example.smartstore.MainActivity
import com.example.smartstore.R
import com.example.smartstore.data.LoginUiState
import com.example.smartstore.ui.theme.CaffeDarkBrown
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.LoginViewModel
import com.ssafy.smartstore.dto.User
import kotlinx.coroutines.*


private const val TAG = "LoginScreen_싸피"
enum class LoginScreen() {
    LoginScreen,
    JoinScreen
}

@Composable
fun LoginApp(
    user:User,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = LoginViewModel(),
    navController: NavController = rememberNavController(),
){
    val context = LocalContext.current
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    Scaffold { innerPadding->
        var startScreen = LoginScreen.LoginScreen.name
        if(user.id != ""){
            //startScreen = LoginScreen
        }
        NavHost(
            navController = navController,
            startDestination = LoginScreen.LoginScreen.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = LoginScreen.LoginScreen.name){
                viewModel.resetState()
                LoginPage(
                    onLoginButtonClicked = { loginState ->
                        var loginUser:User? = null
                        if(loginState.id.isEmpty()){
                            Toast.makeText(context, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }else if(loginState.password.isEmpty()){
                            Toast.makeText(context, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }else{
                            coroutineScope.launch{
                                val job = CoroutineScope(Dispatchers.IO).async{
                                    loginUser = viewModel.checkLogin(loginState)
                                }

                                job.await()

                                if(loginUser != null){
                                    val loginActivity = context as LoginActivity
                                    loginActivity.startActivity(Intent(context, MainActivity::class.java))
                                    loginActivity.finish()
                                    ApplicationClass.sharedPreferencesUtil.addUser(loginUser!!)
                                }else{
                                    Toast.makeText(context, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    },
                    onJoinButtonClicked = {
                        navController.navigate(LoginScreen.JoinScreen.name)
                    }
                )
            }
            composable(route = LoginScreen.JoinScreen.name){
                JoinScreen().JoinPage(viewModel,
                    onDuplicateButtonClicked = { id ->
                        var isDuplicated = false
                        if(id.isEmpty()){
                            Toast.makeText(context, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                            return@JoinPage
                        }

                        coroutineScope.launch {
                            val job = CoroutineScope(Dispatchers.IO).async {
                                isDuplicated = viewModel.checkDuplicate(id)
                            }
                            job.await()

                            Log.d(TAG, "LoginApp: isChecked : ${isDuplicated}")

                            if (isDuplicated) {
                                Toast.makeText(context, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                            }

                        }
                    },
                    onJoinButtonClicked = { loginInfo ->
                        val result = viewModel.checkJoinData(loginInfo)
                        var isRegisted = false
                        if(result == 0){
                            Toast.makeText(context, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }else if(result == 1){
                            Toast.makeText(context, "아이디 중복체크를 주세요.", Toast.LENGTH_SHORT).show()
                        }else if(result == 2){
                            Toast.makeText(context, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }else if(result == 3){
                            Toast.makeText(context, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }else{
                            coroutineScope.launch{
                                val job = CoroutineScope(Dispatchers.IO).async {
                                    isRegisted = viewModel.registUser(loginInfo)
                                }

                                job.await()

                                if(isRegisted){
                                    Toast.makeText(context, "가입 되었습니다.", Toast.LENGTH_SHORT).show()
                                    backToLogin(viewModel, navController)
                                }else{
                                    Toast.makeText(context, "가입 도중 실패했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                )
            }
        }

    }
}

@Composable
fun LoginPage(
    onLoginButtonClicked: (LoginUiState) -> Unit,
    onJoinButtonClicked: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        LoginComponent(
            onLoginButtonClicked = onLoginButtonClicked,
            onJoinButtonClicked = onJoinButtonClicked
        )
    }
}

@Composable
fun LoginComponent(
    onLoginButtonClicked: (LoginUiState) -> Unit,
    onJoinButtonClicked:() ->(Unit)
){
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
    ){
        Image(painter = painterResource(id = R.drawable.logo)
            , contentDescription = "logo",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Login", style = MaterialTheme.typography.h1,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = id,
            onValueChange = { id = it},
            label = { Text("id") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row (
            modifier = Modifier.padding(8.dp)
        ){
            Button(
                onClick = {
                    val loginState = LoginUiState(id, password, "",false)
                    onLoginButtonClicked(loginState)
                    resetLoginInfo(id, password)
                },
                Modifier
                    .background(CaffeDarkBrown, shape = MaterialTheme.shapes.large)
                    .weight(1.0f)
            ) {
                Text("LOGIN", style = MaterialTheme.typography.button,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onJoinButtonClicked() },
                Modifier
                    .background(CaffeDarkBrown, shape = MaterialTheme.shapes.large)
                    .weight(1.0f)
            ){
                Text("JOIN", style = MaterialTheme.typography.button,
                    modifier = Modifier.padding(16.dp))
            }
        }

    }
}

private fun resetLoginInfo(id:String, password:String){

}

// 로그인 화면으로 돌아오기
private fun backToLogin(
    viewModel: LoginViewModel,
    navController: NavController
){
    viewModel.resetState()
    navController.popBackStack(LoginScreen.LoginScreen.name, inclusive = false)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    SmartStoreTheme {
        LoginPage(onLoginButtonClicked = {}, onJoinButtonClicked = {})
    }
}
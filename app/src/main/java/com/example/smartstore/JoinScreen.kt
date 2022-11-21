package com.example.smartstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartstore.data.LoginUiState
import com.example.smartstore.ui.theme.CaffeDarkBrown
import com.example.smartstore.ui.theme.SmartStoreTheme

class JoinScreen {
    @Composable
    fun JoinPage(
        viewModel: LoginViewModel,
        onDuplicateButtonClicked: (String) -> Unit = {},
        onJoinButtonClicked:(LoginUiState) -> Unit = {}
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            JoinComponent(viewModel, onDuplicateButtonClicked, onJoinButtonClicked)
        }
    }

    @Composable
    fun JoinComponent(
        viewModel: LoginViewModel,
        onDuplicateButtonClicked:(String) -> Unit = {},
        onJoinButtonClicked:(LoginUiState) -> Unit = {}
    ){
        var isChecked by remember{ mutableStateOf(false) }
        var id by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var nickname by remember { mutableStateOf("") }
        Column(
        ){
            Image(painter = painterResource(id = R.drawable.logo)
                , contentDescription = "logo",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Join", style = MaterialTheme.typography.h1,
                modifier = Modifier.align(Alignment.CenterHorizontally))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(64.dp)
            ) {
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("id") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.weight(1.0f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        onDuplicateButtonClicked(id)
                        isChecked = viewModel.uiState.value.isChecked
                    },
                    Modifier
                        .background(CaffeDarkBrown)
                        .padding(8.dp)
                        .width(32.dp)
                        .height(32.dp)
                ) {
                    Icon(
                        painter = if(!isChecked){
                            painterResource(id = R.drawable.check_mark)
                        }else{
                            painterResource(id = R.drawable.check)
                         },
                        contentDescription = null,
                        Modifier.fillMaxHeight().fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it},
                label = { Text("nickname") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ){
                Button(
                    onClick = {
                        val loginState = LoginUiState(id, password, nickname, isChecked)
                        onJoinButtonClicked(loginState)
                  },
                    Modifier
                        .background(CaffeDarkBrown, shape = MaterialTheme.shapes.large)
                        .width(150.dp)
                ) {
                    Text("JOIN", style = MaterialTheme.typography.button,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SmartStoreTheme {
            JoinPage(viewModel = viewModel())
        }
    }
}
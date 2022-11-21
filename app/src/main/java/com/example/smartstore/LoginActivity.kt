package com.example.smartstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstore.ui.theme.Cafe24face
import com.example.smartstore.ui.theme.SmartStoreTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginPage()
                }
            }
        }
    }
}

@Composable
fun LoginPage() {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(32.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        LoginComponent()
    }
}

@Composable
fun LoginComponent(){
    Column(
    ){
        Image(painter = painterResource(id = R.drawable.logo)
            , contentDescription = "logo",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .align(CenterHorizontally))
        Text("Login", style = MaterialTheme.typography.h1,
            modifier = Modifier.align(CenterHorizontally))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {Text("id")},
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {Text("password")},
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Button(onClick = {  }) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    SmartStoreTheme {
        LoginPage()
    }
}
package com.example.smartstore.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartstore.ui.theme.CaffeMenuBack
import com.example.smartstore.ui.theme.SmartStoreTheme

@Composable
fun HomeScreen(){
    Column(
        modifier = Modifier.fillMaxWidth()
                        .fillMaxHeight()
            .background(Color.White)
    ) {
        Text("home")
    }
}

@Preview
@Composable
fun HomePreview(){
    SmartStoreTheme {
        HomeScreen()
    }
}
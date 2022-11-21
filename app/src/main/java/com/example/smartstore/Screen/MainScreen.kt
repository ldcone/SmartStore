package com.example.smartstore.Screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartstore.Greeting
import com.example.smartstore.ui.theme.SmartStoreTheme

class MainScreen {
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SmartStoreTheme {
        Greeting("Android")
    }
}
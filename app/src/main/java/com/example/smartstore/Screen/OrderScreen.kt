package com.example.smartstore.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartstore.R
import com.example.smartstore.dto.Product
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
 fun OrderScreen(viewModel: MainViewModel){
    val numbers = (0..20).toList()
    val coroutineScope = rememberCoroutineScope()
    var prodlist = viewModel.allProduct.value
    Log.d("orderScreen","$prodlist")


//    val item = viewModel.allProduct
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .padding(
                    top = 30.dp,
                    bottom = 30.dp
                )
                .align(Alignment.CenterHorizontally)
        ){
            Text(
                "매장과의 거리가 350m 입니다.",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .padding(start = 2.dp, bottom = 5.dp)
            )
        }
        Text(
            text = "MENU",
            Modifier.padding(start = 30.dp, bottom = 30.dp),
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            fontSize = 28.sp,
        )
        Grid(prodlist = prodlist)
        FloatingActionButton()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Grid(prodlist : List<Product>?){
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        modifier = Modifier.height(500.dp)
    ){
        if(null != prodlist){
            items(prodlist.size){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .padding(start = 2.dp, bottom = 5.dp)
                    )
                    Text(text = prodlist[it].name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }else{
            items(10){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .padding(start = 2.dp, bottom = 5.dp)
                    )
                    Text(text = "error", fontSize = 14.sp)
                }
            }
        }


    }
}

@Composable
fun FloatingActionButton(){
    val ctx = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .fillMaxSize()
            .padding(bottom = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = {},
            shape = CircleShape,
            backgroundColor = Color.Yellow,
            contentColor = Color.Black
        ) {
            Icon(Icons.Filled.ShoppingCart, "")
        }
    }
}

@Preview
@Composable
fun OrderPreview(){
    SmartStoreTheme {
        OrderScreen(viewModel())
    }
}

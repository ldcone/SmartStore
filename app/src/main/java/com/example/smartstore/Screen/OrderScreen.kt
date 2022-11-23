package com.example.smartstore.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartstore.ApplicationClass
import com.example.smartstore.R
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.ssafy.smartstore.dto.Product

enum class OrderScreen(){
    OrderScreen,
    OrderDetailScreen
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrderApp(viewModel: MainViewModel){
    val navController = rememberNavController()
    Scaffold {
        NavHost(
            navController = navController,
            startDestination = OrderScreen.OrderScreen.name,
        ){
            composable(route = OrderScreen.OrderScreen.name){
                OrderScreen(
                    viewModel,
                    onItemClicked = {
                        viewModel.Product = it
                        navController.navigate(OrderScreen.OrderDetailScreen.name)
                    }
                )
            }
            composable(route = OrderScreen.OrderDetailScreen.name){
                OrderDetailScreen(viewModel = viewModel )
            }
        }
    }
}



@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
 fun OrderScreen(viewModel: MainViewModel, onItemClicked:(Product)->(Unit)){
    val navController = rememberNavController()
    val prodlist = viewModel.allProduct.value
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
        Grid(prodlist = prodlist,viewModel,onItemClicked)
        FloatingActionButton()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Grid(prodlist : List<Product>?,viewModel: MainViewModel,onItemClicked: (Product) -> Unit){
    LazyVerticalGrid(
        GridCells.Fixed(3),
        modifier = Modifier.height(500.dp),
    ){
        if(null != prodlist){
            items(prodlist.size){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                       val prod = prodlist[prodlist.size -it -1]
                        Log.d("orderClicked","$prod")
                        onItemClicked(prod)
                    }
                )
                {
                    GlideImage(
                        imageModel = "${ApplicationClass.MENU_IMGS_URL}${prodlist[prodlist.size-it-1].img}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(120.dp)
                            .height(160.dp)
                            .clip(RoundedCornerShape(20.dp))
                        )
                    Text(
                        text = prodlist[prodlist.size-it-1].name,
                        modifier = Modifier.padding(top = 5.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
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
        OrderScreen(viewModel(),{})
    }
}

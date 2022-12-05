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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartstore.ApplicationClass
import com.example.smartstore.MainActivity
import com.example.smartstore.R
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.ssafy.smartstore.dto.Product
import com.ssafy.smartstore.dto.ShoppingCart

enum class OrderScreen(){
    OrderScreen,
    OrderDetailScreen,
    ShoppingCartScreen,
    MapScreen
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrderApp(viewModel: MainViewModel){
    val navController = rememberNavController()
    val mainActivity = LocalContext.current as MainActivity

    // bottom navigation bar shown
    viewModel.setVisibleBottomNav(true)

    // 현재 위치 가져오기
    mainActivity.getCurrentLocation()

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = OrderScreen.OrderScreen.name,
        ){
            composable(route = OrderScreen.OrderScreen.name){
                OrderScreen(
                    viewModel,
                    toDetail = {
                        viewModel.Product = it
                        navController.navigate(OrderScreen.OrderDetailScreen.name)
                    },
                    toShoppingCart={
                        navController.navigate(OrderScreen.ShoppingCartScreen.name)
                    },
                    toMap={
                        navController.navigate(OrderScreen.MapScreen.name)
                    }
                )
            }
            composable(route = OrderScreen.OrderDetailScreen.name){
                OrderDetailScreen(viewModel = viewModel,
                    onItemClick = {
                        navController.popBackStack(OrderScreen.OrderScreen.name, inclusive = false)
                    })
            }
            composable(route = OrderScreen.ShoppingCartScreen.name){
                ShoppingCartScreen(viewModel = viewModel,
                    onItemClicked ={
                        navController.popBackStack(OrderScreen.OrderScreen.name, inclusive = false)
                    } )
            }
            composable(route = OrderScreen.MapScreen.name){
                MapScreen(viewModel = viewModel)
            }
        }
    }
}



@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
 fun OrderScreen(viewModel: MainViewModel,
                 toDetail:(Product)->(Unit),
                 toShoppingCart: ()->(Unit),
                 toMap:() -> (Unit)
){
    val navController = rememberNavController()
//    val prodlist = viewModel.allProduct
    val prodlist2 by viewModel.allProduct.observeAsState(listOf())
    val mainActivity = (LocalContext.current) as MainActivity
    val distance by viewModel.distanceToCafe.observeAsState()
    Log.d("orderScreen","$prodlist2")
//    val item = viewModel.allProduct
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    top = 30.dp,
                    bottom = 30.dp
                )
        ){
            Text(
                if(distance == null){
                    "위치 정보를 불러오는 중입니다."
                }else{
                    "매장과의 거리가 ${distance?:0}m 입니다."
                 },
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.map),
                contentDescription = "map",
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .clickable {
                        toMap()
                    }
            )
        }
        Text(
            text = "MENU",
            Modifier.padding(start = 30.dp, bottom = 30.dp),
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            fontSize = 28.sp,
        )
        Grid(prodlist = prodlist2,viewModel,toDetail)
        FloatingActionButton(toShoppingCart)
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
                        viewModel.getProductCommentInfo(prod.id)
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
fun FloatingActionButton(onItemClicked: () -> Unit){
    val ctx = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .fillMaxSize()
            .padding(end = 23.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = {onItemClicked()},
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
//        OrderScreen(viewModel(),{})
    }
}

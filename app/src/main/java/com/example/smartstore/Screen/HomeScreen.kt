package com.example.smartstore.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartstore.ApplicationClass
import com.example.smartstore.ui.theme.CaffeDarkBrown
import com.example.smartstore.ui.theme.CaffeMenuBack
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.ssafy.smartstore.dto.User
import com.ssafy.smartstore.dto.UserOrderDetail
import com.ssafy.smartstore.response.LatestOrderResponse
import com.ssafy.smartstore.util.CommonUtils
import java.util.*

private const val TAG = "HomeScreen_싸피"

enum class HomeScreen(){
    HomeScreen,
    ShoppingCartScreen
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeApp(
    user:User,
    viewModel:MainViewModel
){
    val navController = rememberNavController()
    Scaffold {
        NavHost(
            navController = navController,
            startDestination = HomeScreen.HomeScreen.name
        ){
            composable(route = HomeScreen.HomeScreen.name){
                HomeScreen(user, viewModel,
                toShoppingCart = {
                    navController.navigate(HomeScreen.ShoppingCartScreen.name)
                })
            }
            composable(route = HomeScreen.ShoppingCartScreen.name){
                ShoppingCartScreen(viewModel = viewModel,
                    onItemClicked = {
                        navController.popBackStack(HomeScreen.HomeScreen.name, inclusive = false)
                    })
            }
        }
    }
}

@Composable
fun HomeScreen(
    user:User,
    viewModel:MainViewModel,
    toShoppingCart: () -> Unit
){
    val recentList by viewModel.allRecentOrder.observeAsState(listOf())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("${user.name} 님", style = MaterialTheme.typography.h3)
        Spacer(Modifier.height(8.dp))
        Text("좋은 하루 보내세요.\uD83E\uDD70", style = MaterialTheme.typography.body1)
        Spacer(Modifier.height(32.dp))
        Text("알림판", style = MaterialTheme.typography.h4)
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(
                    width = 2.dp,
                    color = CaffeDarkBrown,
                    shape = MaterialTheme.shapes.large
                )){

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("최근 주문", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            RecentGridLayout(list = recentList, viewModel, toShoppingCart)
        }
    }
}

@Composable
fun RecentGridLayout(
    list: List<LatestOrderResponse>?,
    viewModel: MainViewModel,
    toShoppingCart: () -> Unit
){
    LazyRow{
        if(list != null){
            items(list.size){
                RecentGridItem(item = list[it], viewModel, toShoppingCart)
                Spacer(modifier = Modifier.width(16.dp))
            }
        }else{
            Log.d(TAG, "RecentGridLayout: null data")
        }
    }
}

@Composable
fun RecentGridItem(
    item:LatestOrderResponse,
    viewModel: MainViewModel,
    toShoppingCart:() -> Unit
){
    Card(
        Modifier
            .width(180.dp)
            .fillMaxHeight()
            .border(
                width = 2.dp,
                color = CaffeDarkBrown,
                shape = MaterialTheme.shapes.large
            ).clickable {
                viewModel.addShopCartWithLatestOrder(item)
                toShoppingCart()
            }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .clip(MaterialTheme.shapes.large)
                    .width(100.dp)
                    .height(100.dp)
                    .background(CaffeMenuBack),
                contentAlignment = Alignment.Center
            ){
                GlideImage(
                    imageModel = "${ApplicationClass.MENU_IMGS_URL}${item.img}",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(90.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            Spacer(Modifier.height(16.dp))
            Text("${item.productName} 외 ${item.orderCnt - 1} 잔", style = MaterialTheme.typography.subtitle1)
            Spacer(Modifier.height(8.dp))
            Text("${CommonUtils.makeComma(item.totalPrice)}", style = MaterialTheme.typography.subtitle1)
            Spacer(Modifier.height(8.dp))
            Text("${CommonUtils.getFormattedItemDate(item.orderDate)}", style = MaterialTheme.typography.subtitle1)
        }
    }

}

@Preview
@Composable
fun HomePreview(){
    SmartStoreTheme {
        //RecentGridItem(LatestOrderResponse("",0,"",0,"", Date(),'N',0,"",0))
        //HomeScreen(User("test","유저01","1234",0), viewModel())
    }
}
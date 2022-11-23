package com.example.smartstore.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartstore.ApplicationClass
import com.example.smartstore.R
import com.example.smartstore.ui.theme.CaffeBrown
import com.example.smartstore.ui.theme.CaffeDarkBrown
import com.example.smartstore.ui.theme.CaffeMenuBack
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.ssafy.smartstore.dto.User
import com.ssafy.smartstore.response.LatestOrderResponse
import com.ssafy.smartstore.util.CommonUtils

private const val TAG = "MyPageScreen_싸피"

enum class MyPageScreens(){
    MyPageScreen,
    MyOrderDetailScreen,
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyPageApp(
    user:User,
    viewModel:MainViewModel
){
    val context = LocalContext.current
    val navController = rememberNavController()
    Scaffold{
        NavHost(
            navController = navController,
            startDestination = MyPageScreens.MyPageScreen.name,
        ){
            composable(route = MyPageScreens.MyPageScreen.name){
                MyPageScreen(
                    user,
                    viewModel,
                    onOrderItemClicked = {
                        viewModel.Order = it
                        navController.navigate(MyPageScreens.MyOrderDetailScreen.name)
                    },
                    onLogOutClicked = {
                        viewModel.logout(context)
                    }
                )
            }
            composable(route = MyPageScreens.MyOrderDetailScreen.name){
                MyOrderDetailScreen(viewModel)
            }
        }
    }
}

@Composable
fun MyPageScreen(
    user: User,
    viewModel: MainViewModel,
    onOrderItemClicked:(LatestOrderResponse)->Unit,
    onLogOutClicked:()->Unit
){
    val grade = viewModel.gradeInfo
    val orderList by viewModel.allRecentOrder.observeAsState(listOf())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(80.dp)
        ){
            Image(painter = painterResource(id = R.drawable.user),
                contentDescription = null,
                Modifier
                    .weight(1.0f)
                    .width(50.dp)
                    .height(50.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(3.0f)
                    .height(64.dp)
            ) {
                Text("${user.name}님", style = MaterialTheme.typography.h3)
                Spacer(modifier = Modifier.height(4.dp))
                Text("안녕하세요.", style = MaterialTheme.typography.body1)
            }
            Image(painter = painterResource(id = R.drawable.logout),
                contentDescription = null,
                Modifier
                    .weight(1.0f)
                    .width(40.dp)
                    .height(40.dp)
                    .clickable {
                        onLogOutClicked()
                    }
            )
        }
        Column(
            Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                GlideImage(
                    imageModel = "${ApplicationClass.GRADE_IMGS_URL}${grade!!.img}",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("${grade!!.title} ${grade!!.step}단계", style = MaterialTheme.typography.h4)
                Spacer(Modifier.width(4.dp))
                LinearProgressIndicator(
                    progress = when(grade!!.title){
                                    "씨앗" -> ((10 - grade!!.to).toFloat() / 10)
                                    "꽃" -> ((15 - (grade!!.to)).toFloat() / 15)
                                    "열매" -> ((20 - (grade!!.to)).toFloat() / 20)
                                    "커피콩" -> ((25 - (grade!!.to).toFloat() / 25))
                                    else -> 1.0f
                               },
                    backgroundColor = Color.LightGray,
                    color = CaffeBrown,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )
                Spacer(Modifier.width(4.dp))
                Text(when(grade!!.title){
                        "씨앗" -> "${10 - grade!!.to} / 10"
                        "꽃" -> "${15 - grade!!.to} / 15"
                        "열매" -> "${20 - grade!!.to} / 20"
                        "커피콩" -> "${25 - grade!!.to} / 25"
                        else -> ""
                    },
                    style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth())
            }
            Spacer(Modifier.height(4.dp))
            Text("다음 레벨까지 ${grade!!.to}잔 남았습니다.",
                style = TextStyle(color = Color.Gray, fontSize = 15.sp))
        }
        Image(painter = painterResource(id = R.drawable.space),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("주문내역", style = MaterialTheme.typography.h3)
        Spacer(modifier = Modifier.height(16.dp))
        OrderListLayout(orderList, onOrderItemClicked)
    }
}

@Composable
fun OrderListLayout(list:List<LatestOrderResponse>, onItemClicked: (LatestOrderResponse) -> Unit){
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        LazyRow{
            if(list != null){
                items(list.size){
                    OrderGridItem(item = list[it], onItemClicked)
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }else{
                Log.d(TAG, "OrderGridLayout: null data")
            }
        }
    }
}

@Composable
fun OrderGridItem(item:LatestOrderResponse, onItemClicked:(LatestOrderResponse)->Unit){
    Card(
        Modifier
            .width(180.dp)
            .fillMaxHeight()
            .border(
                width = 2.dp,
                color = CaffeDarkBrown,
                shape = MaterialTheme.shapes.large
            )
            .clickable {
                onItemClicked(item)
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
fun MyPagePreview(){
    SmartStoreTheme {
        MyPageApp(User("","김싸피","",0), viewModel = viewModel())
    }
}
package com.example.smartstore.Screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartstore.ApplicationClass
import com.example.smartstore.ui.theme.CaffeLightWhite
import com.example.smartstore.ui.theme.CaffeMenuBack
import com.example.smartstore.ui.theme.CaffePointRed
import com.example.smartstore.viewmodel.MainViewModel
import com.example.smartstore.viewmodel.MyOrderDetailViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.ssafy.smartstore.response.OrderDetailResponse
import com.ssafy.smartstore.util.CommonUtils

private const val TAG = "MyOrderDetailScreen_싸피"
@Composable
fun MyOrderDetailScreen(
    mainViewModel:MainViewModel
){
    val order = mainViewModel.Order
    val viewModel = MyOrderDetailViewModel(order)
    val list by viewModel.orderDetailList.observeAsState(listOf())
    viewModel.getOrderDetail()

    mainViewModel.setVisibleBottomNav(false)

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("주문상세",
            style = TextStyle(fontSize = 25.sp, color = CaffePointRed,
                            fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(32.dp))
        Text(if(order?.orderCompleted == 'N'){"준비중"}else{"주문완료"},
            style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(4.dp))
        Text(CommonUtils.getFormattedItemDate(order!!.orderDate), style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(4.dp))
        Text(CommonUtils.makeComma(order.totalPrice), style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(16.dp))
        OrderDetailGridLayout(list)
    }
}

@Composable
fun OrderDetailGridLayout(list:List<OrderDetailResponse>){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ){
        items(list.size){
            OrderDetailItem(item = list[it])
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun OrderDetailItem(item:OrderDetailResponse){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(CaffeLightWhite)
    ){
        Box(
            Modifier
                .clip(MaterialTheme.shapes.large)
                .width(100.dp)
                .height(100.dp)
                .background(CaffeMenuBack)
                .weight(1.0f),
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
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1.0f)
        ) {
            Text(text = "${item.productName}", style = MaterialTheme.typography.subtitle1)
            Spacer(Modifier.height(32.dp))
            Text(text = "${CommonUtils.makeComma(item.unitPrice)}", style = MaterialTheme.typography.subtitle1)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1.0f)
        ) {
            Text(text = "${item.quantity}" + if(item.productType == "coffee"){" 잔"} else{" 개"},
                style = MaterialTheme.typography.subtitle1)
            Spacer(Modifier.height(32.dp))
            Text(text = "${CommonUtils.makeComma(item.totalPrice)}", style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Preview
@Composable
fun MyOrderDetailPreview(){
    MyOrderDetailScreen(viewModel())
}
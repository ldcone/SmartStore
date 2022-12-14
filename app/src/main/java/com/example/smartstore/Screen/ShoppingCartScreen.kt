package com.example.smartstore.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartstore.ApplicationClass
import com.example.smartstore.ui.theme.CaffeDarkBrown
import com.example.smartstore.ui.theme.CaffeLightWhite
import com.example.smartstore.ui.theme.CaffeMenuBack
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.ssafy.smartstore.dto.Order
import com.ssafy.smartstore.dto.OrderDetail
import com.ssafy.smartstore.dto.Product
import com.ssafy.smartstore.dto.ShoppingCart
import com.ssafy.smartstore.response.OrderDetailResponse
import com.ssafy.smartstore.util.CommonUtils
import com.ssafy.smartstore.util.SharedPreferencesUtil

@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
 fun ShoppingCartScreen( viewModel: MainViewModel,onItemClicked:()->(Unit)){
    // bottom navigation bar shown
    viewModel.setVisibleBottomNav(false)

    val shopList = viewModel.getShoppingCart().observeAsState(mutableListOf())
    val mContext = LocalContext.current
    var price =0
    for(item in shopList.value!!){
        price+=item.totalPrice
    }
    Log.d("shop","$shopList")
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
        ){
            Text(
                text = "????????????",
                Modifier.padding(start = 24.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                fontSize = 28.sp,
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(7f)
            ){
                ShopGridLayout(list= shopList,viewModel )
            }
            Row(Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "??? ${shopList.value.size}???", style = MaterialTheme.typography.h4)
                Text(text= "${CommonUtils.makeComma(price)}", style=MaterialTheme.typography.h4)
            }
            Button(onClick = {
                makeOrderData(viewModel = viewModel, list =shopList,mContext,onItemClicked )
                             },
            Modifier.fillMaxWidth()) {
                Text("??????")
            }


        }
    }
}

private fun makeOrderData(
    viewModel: MainViewModel,
    list: State<MutableList<ShoppingCart>>?,
    mContext: Context,
    onItemClicked: () -> Unit
){
    val userId = SharedPreferencesUtil(mContext).getUser().id
    val tableNum = 0
    val listDetail = arrayListOf<OrderDetail>()
    for(item in list?.value!!){
        listDetail.add(OrderDetail(item.menuId, item.menuCnt))
    }
     val order = Order(0, userId, tableNum.toString(),
        null, "N", listDetail)
    completedOrder(viewModel,order,mContext,onItemClicked)
}

private fun completedOrder(
    viewModel: MainViewModel,
    order: Order,
    mContext: Context,
    onItemClicked: () -> Unit
){
    val temp = viewModel.getShoppingCart()
    if(temp.value?.size== 0){
        Toast.makeText(mContext,"???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show()
    }else{
        viewModel.completeOrder(order,mContext)
        onItemClicked()
    }
}

@Composable
fun ShopGridLayout(list:State<MutableList<ShoppingCart>>?,viewModel: MainViewModel){
    LazyColumn(
        Modifier.fillMaxWidth().fillMaxHeight()
            .border(
                BorderStroke(2.dp, CaffeDarkBrown),
                shape = MaterialTheme.shapes.large)
    ){
        if(list != null){
            items(list.value.size){
                ShopGridItem(item = list.value[it],viewModel,list)
                Spacer(modifier = Modifier.width(16.dp))
            }
        }else{
            Log.d("Shop", "RecentGridLayout: null data")
        }
    }
}


@Composable
fun ShopGridItem(item: ShoppingCart,viewModel: MainViewModel,list: State<MutableList<ShoppingCart>>?){
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
                imageModel = "${ApplicationClass.MENU_IMGS_URL}${item.menuImg}",
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
            Text(text = "${item.menuName}", style = MaterialTheme.typography.subtitle1)
            Spacer(Modifier.height(32.dp))
            Text(text = "${CommonUtils.makeComma(item.menuPrice)}", style = MaterialTheme.typography.subtitle1)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1.0f)
        ) {
            Text(text = "${item.menuCnt}" + if(item.type == "coffee"){" ???"} else{" ???"},
                style = MaterialTheme.typography.subtitle1)
            Spacer(Modifier.height(32.dp))
            Text(text = "${CommonUtils.makeComma(item.totalPrice)}", style = MaterialTheme.typography.subtitle1)
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick =
            {
                viewModel.removeShop(item)
            },
            Modifier
                .padding(start = 3.dp)
                .width(32.dp)
                .height(32.dp)
        ) {
            Icon(
                Icons.Filled.Close,
                contentDescription = null,
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}


@Preview
@Composable
fun ShoppingCartPreview(){
    SmartStoreTheme {
//        ShoppingCartScreen({})
//        viewModel(),
    }
}

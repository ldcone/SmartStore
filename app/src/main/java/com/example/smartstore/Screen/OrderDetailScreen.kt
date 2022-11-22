package com.example.smartstore.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import com.example.smartstore.ApplicationClass
import com.example.smartstore.R
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.ssafy.smartstore.dto.Product
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
 fun OrderDetailScreen(viewModel: MainViewModel){
//    var prodlist = viewModel.allProduct.value
//    val prod = viewModel.Product!!
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
//        Row(
//            modifier = Modifier
//                .padding(
//                    bottom = 30.dp
//                )
//                .align(Alignment.CenterHorizontally)
//        ){
//            GImage(prod = prod)
//        }
        Text(
            "커피",
            modifier = Modifier
                .padding(start = 25.dp, bottom = 20.dp),
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
            )
        Row(
            Modifier
                .padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
        ){
            Text(text = "가격", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Text(text = "1,500원",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 200.dp)
            )
        }

        Row(
            Modifier
                .padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
        ){
            Text(text = "수량", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            IconButton(
                onClick = { /*TODO*/ },
                Modifier
                    .padding(start = 190.dp)
                    .width(32.dp)
                    .height(32.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }

            Text(text = "0",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 3.dp)
            )
            IconButton(
                onClick = { /*TODO*/ },
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
}

@Composable
fun GImage(prod:Product){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Magenta)){
        GlideImage(
            imageModel = "${ApplicationClass.MENU_IMGS_URL}${prod.img}",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        )
    }

}

@Preview
@Composable
fun OrderDetailPreview(){
    SmartStoreTheme {
        OrderDetailScreen(viewModel())
    }
}

package com.example.smartstore.Screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartstore.ApplicationClass
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.MainViewModel
import com.gowtham.ratingbar.RatingBar
import com.skydoves.landscapist.glide.GlideImage
import com.ssafy.smartstore.dto.Product
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.ssafy.smartstore.dto.ShoppingCart
import com.example.smartstore.ui.theme.*
import com.ssafy.smartstore.dto.Comment
import com.ssafy.smartstore.response.MenuDetailWithCommentResponse
import com.ssafy.smartstore.util.CommonUtils
import com.ssafy.smartstore.util.SharedPreferencesUtil


@SuppressLint("CoroutineCreationDuringComposition", "RememberReturnType", "SuspiciousIndentation")
@OptIn(ExperimentalFoundationApi::class)
@Composable
 fun OrderDetailScreen(viewModel: MainViewModel,onItemClick:()->(Unit)){
    val prod = viewModel.Product!!
    var rating : Float by remember {
        mutableStateOf(3.0f)
    }
    var counts: Int by remember {
        mutableStateOf(0)
    }
    var comment by remember { mutableStateOf("") }
    val mContext = LocalContext.current
    val commentList = viewModel.commentList.observeAsState(mutableListOf())
    val userId = SharedPreferencesUtil(mContext).getUser().id
    val focusRequester by remember {
        mutableStateOf(FocusRequester())
    }
    val focusManager = LocalFocusManager.current
    Log.d("comment","${commentList.value}")

    // bottom navigation bar shown
    viewModel.setVisibleBottomNav(false)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    bottom = 30.dp
                )
                .align(Alignment.CenterHorizontally)
        ){
            GImage(prod = prod)
        }
        Text(
            "${prod.name}",
            modifier = Modifier
                .padding(start = 25.dp, bottom = 20.dp),
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
            )
        Row(
            Modifier
                .padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
        ){
            Text(text = "가격",Modifier.weight(2.4f), fontSize = 25.sp, fontWeight = FontWeight.Bold,)
            Text(text = "${CommonUtils.makeComma(prod.price)}",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
        ){
            Text(text = "수량",Modifier.weight(2.3f), fontSize = 25.sp, fontWeight = FontWeight.Bold)
            IconButton(
                onClick = { counts++ },
                Modifier
                    .padding(start = 200.dp)
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

            Text(text = "$counts",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 3.dp)
            )
            IconButton(
                onClick =
                {
                    if(counts ==0) return@IconButton
                    counts--
                },
                Modifier
                    .padding(start = 3.dp)
                    .width(32.dp)
                    .height(32.dp)
            ) {
                Icon(
                    Icons.Filled.Remove,
                    contentDescription = null,
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .background(CaffeMenuBack)
        ){
          Row {
              Text(text="평점",
                  Modifier.padding(start = 10.dp),
                  fontWeight = FontWeight.Bold,
                  fontSize = 25.sp
              )
              Text(text="3.2점",
                  Modifier.padding(start = 7.dp, top = 2.dp),
                  fontSize = 20.sp
              )
              RatingBar(value = rating,
                  onValueChange ={
                      rating=it
                  },
                  onRatingChanged ={
                      Log.d("Rating","onRatingChanged:$it")
                  },
                  modifier = Modifier.padding(start = 130.dp)
              )
          }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center) {
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("") },
                maxLines = 1,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = CaffeDarkBrown,
                    fontWeight = FontWeight.Bold
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = CaffeBrown,
                    unfocusedBorderColor = CaffeBrownLight
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .focusRequester(focusRequester = focusRequester)
            )
            Button(
                onClick = {
                    if(comment.isEmpty()){
                        Toast.makeText(mContext,"코멘트를 넣어주세요", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val temp = Comment(0,userId,prod.id,rating,comment)
                          viewModel.addComment(temp)
                    comment=""
                    focusManager.clearFocus()
                          },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 10.dp, start = 10.dp)
            ) {
                Text("등록")
            }
        }
        Column(
            Modifier.padding(8.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.White)
                    .weight(6f)
            ){
                CommentGridLayout(viewModel = viewModel, list = commentList,userId )
            }
            Button(onClick = {
                if(counts ==0){
                    Toast.makeText(mContext,"수량이 없어 담을수 없습니다.",Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val temp = ShoppingCart(prod.id,
                    prod.img,
                    prod.name,
                    counts,
                    prod.price,
                    prod.price*counts,
                    prod.type)
                viewModel.addShop(temp)
                onItemClick() },
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            )
            {
                Text(text = "담기", fontSize = 15.sp)
            }
        }

    }
}

@Composable
fun CommentGridLayout(
    viewModel: MainViewModel,
    list: State<MutableList<MenuDetailWithCommentResponse>>,
    userId: String
){
    LazyColumn(
        Modifier
            .fillMaxHeight()
            .padding(8.dp)
    ){
        items(list.value.size){
            if(list.value[0].productCommentTotalCnt ==0){
                Log.d("comment","isEmpty")
            }else{
                CommentGridItem(viewModel,list.value[it],userId)
            }

        }
    }
}

@Composable
fun CommentGridItem(
    viewModel: MainViewModel,
    comment: MenuDetailWithCommentResponse,
    userId: String
){
    Row(Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween){
        Spacer(modifier = Modifier.width(20.dp))
        Text(comment.commentContent!!,
            style = MaterialTheme.typography.h4
        )

        if(comment.userId.equals(userId)){
            IconButton(
                onClick = {
                          viewModel.removeComment(comment.commentId)
                },
                Modifier
                    .padding(start = 3.dp)
                    .width(32.dp)
                    .height(32.dp)
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = null,
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
        }

//        Box(
//            Modifier
//                .fillMaxWidth()
////                .drawBehind {
////                    val strokeWidth = 1 * density
////                    val y = size.height - strokeWidth / 2
////
////                    drawLine(
////                        Color.LightGray,
////                        Offset(0f, y),
////                        Offset(size.width, y),
////                        strokeWidth
////                    )
////                }
//        )
//        {
//
//
//
//        }

    }
}


@Composable
fun GImage(prod:Product){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(CaffeMenuBack)){
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
        OrderDetailScreen(viewModel(),{})
    }
}

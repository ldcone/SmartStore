package com.example.smartstore.Screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.RatingBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.lang.Math.ceil
import java.lang.Math.floor
import java.time.format.TextStyle


@SuppressLint("CoroutineCreationDuringComposition", "RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
 fun OrderDetailScreen(viewModel: MainViewModel){
//    var prodlist = viewModel.allProduct.value
//    val prod = viewModel.Product!!
    var rating : Float by remember {
        mutableStateOf(3.0f)
    }
    var hasFocus by remember { mutableStateOf(false) }
    var comment by remember { mutableStateOf("") }
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

        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.Cyan)
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
//        Card(
//            modifier = Modifier
//                .padding(top = 10.dp)
//                .height(40.dp)
//                .width(270.dp),
//            border = BorderStroke(2.dp, color = Color.Red)
//        ) {
//            OutlinedTextField(
//                value = comment,
//                onValueChange = { comment= it },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
//
//            )
//        }
        Row(
            Modifier.fillMaxWidth().height(80.dp),
            horizontalArrangement = Arrangement.Center) {
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Enter text") },
                maxLines = 1,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Red
                ),
                shape = RoundedCornerShape(12.dp),
            )
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(60.dp).padding(top =10.dp, start = 10.dp)
            ) {
                Text("등록")
            }
        }



    }
}

//@Composable
//fun RatingBar(
//    modifier: Modifier = Modifier,
//    rating: Double = 0.0,
//    stars: Int = 5,
//    starsColor: Color = Color.Yellow,
//) {
//
//    val filledStars = floor(rating).toInt()
//    val unfilledStars = (stars - ceil(rating)).toInt()
//    val halfStar = !(rating.rem(1).equals(0.0))
//
//    Row(modifier = modifier) {
//        repeat(filledStars) {
//            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor)
//        }
//
//        if (halfStar) {
//            Icon(
//                imageVector = Icons.Outlined.Star,
//                contentDescription = null,
//                tint = starsColor
//            )
//        }
//
//        repeat(unfilledStars) {
//            Icon(
//                imageVector = Icons.Outlined.Star,
//                contentDescription = null,
//                tint = starsColor
//            )
//        }
//    }
//}

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

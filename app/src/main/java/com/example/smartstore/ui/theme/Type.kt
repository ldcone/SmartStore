package com.example.smartstore.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.smartstore.R

val Cafe24face = FontFamily(
    Font(R.font.cafe24_ssurround, FontWeight.Bold),
    Font(R.font.cafe24_ssurround_air, FontWeight.Normal)
)

val ElandChoiceface = FontFamily(
    Font(R.font.eland_choice_b, FontWeight.Bold),
    Font(R.font.eland_choice_l, FontWeight.Normal),
    Font(R.font.eland_choice_m, FontWeight.Medium)
)

val ElandNiceface = FontFamily(
    Font(R.font.eland_nice_m, FontWeight.Medium)
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = ElandChoiceface,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp,
        color = CaffeDarkBrown
    ),
    h2 = TextStyle(
        fontFamily = ElandChoiceface,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        color = CaffeDarkBrown
    ),
    h3 = TextStyle(
        fontFamily = ElandChoiceface,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    ),
    h4 = TextStyle(
        fontFamily = ElandChoiceface,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h5 = TextStyle(
        fontFamily = ElandChoiceface,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
    ),
    body1 = TextStyle(
        fontFamily = ElandChoiceface,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        color = CaffeDarkBrown
    ),
    button = TextStyle(
        fontFamily = ElandChoiceface,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = Color.White
    ),
    subtitle1 = TextStyle(
        fontFamily = ElandChoiceface,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = CaffeDarkBrown
    )
)

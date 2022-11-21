package com.example.smartstore.ui.theme

import androidx.compose.material.Typography
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
    body1 = TextStyle(
        fontFamily = ElandChoiceface,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )
)

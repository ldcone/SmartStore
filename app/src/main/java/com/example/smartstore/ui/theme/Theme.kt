package com.example.smartstore.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    background = CaffeLightWhite,
    surface = Color.White,
    onSurface = Color.White,
    primary = CaffeBrownLight,
    primaryVariant = CaffeBrown,
    secondary = CaffeDarkBrown
)

private val LightColorPalette = lightColors(
    background = CaffeLightWhite,
    surface = Color.White,
    onSurface = Color.White,
    primary = CaffeBrownLight,
    primaryVariant = CaffeBrown,
    secondary = CaffeDarkBrown
)

@Composable
fun SmartStoreTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
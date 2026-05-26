package com.hazemafaneh.liveactivitiesexample.pizzatracker

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

object PizzaTheme {
    val bg          = Color(0xFFFBF7F2)
    val surface     = Color(0xFFFFFFFF)
    val ink         = Color(0xFF1A1410)
    val sub         = Color(0xFF776A60)
    val line        = Color(0xFF1A1410).copy(alpha = 0.08f)
    val accent      = Color(0xFFE03A2F)
    val accentInk   = Color(0xFFFFFFFF)
    val accentSoft  = Color(0xFFFDECEA)
    val success     = Color(0xFF1E8E4A)
    val pizzaYellow = Color(0xFFF4C152)
}

object BasilTheme {
    val bg          = Color(0xFFF4F7F1)
    val surface     = Color(0xFFFFFFFF)
    val ink         = Color(0xFF14201A)
    val sub         = Color(0xFF5C6B62)
    val line        = Color(0xFF14201A).copy(alpha = 0.08f)
    val accent      = Color(0xFF2F7D4F)
    val accentInk   = Color(0xFFFFFFFF)
    val accentSoft  = Color(0xFFE6F2EA)
    val success     = Color(0xFF1E8E4A)
    val pizzaYellow = Color(0xFFF4C152)
}

object MidnightTheme {
    val bg          = Color(0xFF0F1115)
    val surface     = Color(0xFF1A1D24)
    val ink         = Color(0xFFF5F1EA)
    val sub         = Color(0xFFA9A39A)
    val line        = Color(0xFFF5F1EA).copy(alpha = 0.12f)
    val accent      = Color(0xFFFF6A57)
    val accentInk   = Color(0xFF0F1115)
    val accentSoft  = Color(0xFFFF6A57).copy(alpha = 0.16f)
    val success     = Color(0xFF4ED38B)
    val pizzaYellow = Color(0xFFF4C152)
}

val displayFont: FontFamily = FontFamily.Serif
val bodyFont: FontFamily = FontFamily.SansSerif

val etaDisplay = TextStyle(
    fontSize = 48.sp,
    fontFamily = displayFont,
    letterSpacing = (-0.04).em,
    lineHeight = 48.sp,
)

val headlineDisplay = TextStyle(
    fontSize = 38.sp,
    fontFamily = displayFont,
    letterSpacing = (-0.035).em,
    lineHeight = 39.sp,
)

val headlineItalic = headlineDisplay.copy(fontStyle = FontStyle.Italic)

val eyebrowStyle = TextStyle(
    fontSize = 12.sp,
    fontFamily = bodyFont,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.06.em,
)

val cardShape   = RoundedCornerShape(24.dp)
val pillShape   = RoundedCornerShape(50)
val buttonShape = RoundedCornerShape(18.dp)
val panelShape  = RoundedCornerShape(20.dp)
val rowShape    = RoundedCornerShape(14.dp)

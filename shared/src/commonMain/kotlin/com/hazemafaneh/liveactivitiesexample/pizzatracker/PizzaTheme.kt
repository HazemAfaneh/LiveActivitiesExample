package com.hazemafaneh.liveactivitiesexample.pizzatracker

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

data class PizzaPalette(
    val bg: Color,
    val surface: Color,
    val ink: Color,
    val sub: Color,
    val line: Color,
    val accent: Color,
    val accentInk: Color,
    val accentSoft: Color,
    val success: Color,
    val pizzaYellow: Color,
    val isDark: Boolean,
)

val LightPalette = PizzaPalette(
    bg          = Color(0xFFFBF7F2),
    surface     = Color(0xFFFFFFFF),
    ink         = Color(0xFF1A1410),
    sub         = Color(0xFF776A60),
    line        = Color(0xFF1A1410).copy(alpha = 0.08f),
    accent      = Color(0xFFE03A2F),
    accentInk   = Color(0xFFFFFFFF),
    accentSoft  = Color(0xFFFDECEA),
    success     = Color(0xFF1E8E4A),
    pizzaYellow = Color(0xFFF4C152),
    isDark      = false,
)

val DarkPalette = PizzaPalette(
    bg          = Color(0xFF0F1115),
    surface     = Color(0xFF1A1D24),
    ink         = Color(0xFFF5F1EA),
    sub         = Color(0xFFA9A39A),
    line        = Color(0xFFF5F1EA).copy(alpha = 0.12f),
    accent      = Color(0xFFFF6A57),
    accentInk   = Color(0xFF0F1115),
    accentSoft  = Color(0xFFFF6A57).copy(alpha = 0.16f),
    success     = Color(0xFF4ED38B),
    pizzaYellow = Color(0xFFF4C152),
    isDark      = true,
)

val LocalPizzaPalette = staticCompositionLocalOf { LightPalette }

@Composable
fun PizzaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val palette = if (darkTheme) DarkPalette else LightPalette
    CompositionLocalProvider(LocalPizzaPalette provides palette, content = content)
}

object PizzaTheme {
    val palette: PizzaPalette
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current

    val bg: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.bg
    val surface: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.surface
    val ink: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.ink
    val sub: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.sub
    val line: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.line
    val accent: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.accent
    val accentInk: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.accentInk
    val accentSoft: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.accentSoft
    val success: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.success
    val pizzaYellow: Color
        @Composable @ReadOnlyComposable get() = LocalPizzaPalette.current.pizzaYellow
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

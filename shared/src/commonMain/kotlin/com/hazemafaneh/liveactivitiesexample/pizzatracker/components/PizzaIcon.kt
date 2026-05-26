package com.hazemafaneh.liveactivitiesexample.pizzatracker.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hazemafaneh.liveactivitiesexample.pizzatracker.PizzaTheme

@Composable
fun PizzaIconMark(
    size: Dp = 44.dp,
    background: Color = PizzaTheme.accentSoft,
    sliceFill: Color = PizzaTheme.pizzaYellow,
    accent: Color = PizzaTheme.accent,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size * 0.62f)) {
            val w = this.size.width
            val h = this.size.height
            val tip = Offset(w / 2f, h * 0.08f)
            val left = Offset(w * 0.08f, h * 0.92f)
            val right = Offset(w * 0.92f, h * 0.92f)
            val slice = Path().apply {
                moveTo(tip.x, tip.y)
                lineTo(left.x, left.y)
                lineTo(right.x, right.y)
                close()
            }
            drawPath(slice, color = sliceFill)
            drawPath(
                slice,
                color = accent,
                style = Stroke(width = 1.5.dp.toPx()),
            )

            val r = w * 0.07f
            drawCircle(accent, radius = r, center = Offset(w * 0.50f, h * 0.40f))
            drawCircle(accent, radius = r, center = Offset(w * 0.36f, h * 0.60f))
            drawCircle(accent, radius = r, center = Offset(w * 0.64f, h * 0.60f))
            drawCircle(accent, radius = r * 0.85f, center = Offset(w * 0.46f, h * 0.78f))
            drawCircle(accent, radius = r * 0.85f, center = Offset(w * 0.58f, h * 0.80f))
        }
    }
}

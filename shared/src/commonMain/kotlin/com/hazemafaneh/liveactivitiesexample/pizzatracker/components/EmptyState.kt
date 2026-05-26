package com.hazemafaneh.liveactivitiesexample.pizzatracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazemafaneh.liveactivitiesexample.pizzatracker.PizzaTheme
import com.hazemafaneh.liveactivitiesexample.pizzatracker.bodyFont

@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
) {
    val corner = 24.dp
    val borderColor = PizzaTheme.line.copy(alpha = 0.5f)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.5.dp.toPx()
                val inset = strokeWidth / 2f
                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - strokeWidth, size.height - strokeWidth),
                    cornerRadius = CornerRadius(corner.toPx(), corner.toPx()),
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f),
                    ),
                )
            }
            .padding(horizontal = 20.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = "🍕", fontSize = 32.sp)
        Text(
            text = "No active order",
            fontSize = 14.sp,
            fontFamily = bodyFont,
            fontWeight = FontWeight.SemiBold,
            color = PizzaTheme.ink,
        )
        Text(
            text = "Start a new order to track it live.",
            fontSize = 12.sp,
            fontFamily = bodyFont,
            color = PizzaTheme.sub,
        )
    }
}

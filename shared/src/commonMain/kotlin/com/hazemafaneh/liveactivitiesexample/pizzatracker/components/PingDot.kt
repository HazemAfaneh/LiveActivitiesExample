package com.hazemafaneh.liveactivitiesexample.pizzatracker.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PingDot(
    color: Color,
    dotSize: Float = 8f,
    boxSize: Float = 16f,
) {
    val transition = rememberInfiniteTransition(label = "ping")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 2.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ping-scale",
    )
    val alpha by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ping-alpha",
    )

    Box(
        modifier = Modifier.size(boxSize.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(boxSize.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = (dotSize / 2f).dp.toPx()
            drawCircle(
                color = color.copy(alpha = alpha),
                radius = baseRadius * scale,
                center = center,
            )
            drawCircle(
                color = color,
                radius = baseRadius,
                center = center,
            )
        }
    }
}

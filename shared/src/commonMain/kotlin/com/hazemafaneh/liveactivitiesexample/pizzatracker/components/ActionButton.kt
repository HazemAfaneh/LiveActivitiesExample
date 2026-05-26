package com.hazemafaneh.liveactivitiesexample.pizzatracker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hazemafaneh.liveactivitiesexample.pizzatracker.PizzaTheme
import com.hazemafaneh.liveactivitiesexample.pizzatracker.bodyFont
import com.hazemafaneh.liveactivitiesexample.pizzatracker.buttonShape

@Composable
fun ActionButton(
    hasOrder: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.985f else 1f,
        animationSpec = tween(120),
        label = "press-scale",
    )

    val container by animateColorAsState(
        targetValue = if (hasOrder) PizzaTheme.surface else PizzaTheme.accent,
        animationSpec = tween(200),
        label = "btn-bg",
    )
    val content by animateColorAsState(
        targetValue = if (hasOrder) PizzaTheme.ink else PizzaTheme.accentInk,
        animationSpec = tween(200),
        label = "btn-fg",
    )

    Button(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (hasOrder) 0.dp else 6.dp,
                shape = buttonShape,
                spotColor = PizzaTheme.accent.copy(alpha = 0.45f),
                ambientColor = Color.Transparent,
            ),
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
        ),
        border = if (hasOrder) BorderStroke(1.dp, PizzaTheme.line) else null,
    ) {
        Text(
            text = if (hasOrder) "End live activity" else "Start new order",
            fontSize = 15.sp,
            fontFamily = bodyFont,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.01).em,
        )
    }
}

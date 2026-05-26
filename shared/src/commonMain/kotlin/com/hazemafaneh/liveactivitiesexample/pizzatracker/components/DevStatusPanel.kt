package com.hazemafaneh.liveactivitiesexample.pizzatracker.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hazemafaneh.liveactivitiesexample.pizzatracker.PizzaTheme
import com.hazemafaneh.liveactivitiesexample.pizzatracker.STATUSES
import com.hazemafaneh.liveactivitiesexample.pizzatracker.StatusKey
import com.hazemafaneh.liveactivitiesexample.pizzatracker.bodyFont
import com.hazemafaneh.liveactivitiesexample.pizzatracker.panelShape
import com.hazemafaneh.liveactivitiesexample.pizzatracker.rowShape

@Composable
fun DevStatusPanel(
    current: StatusKey,
    onSelect: (StatusKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionHeader(label = "UPDATE STATUS") {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = PizzaTheme.line,
            ) {
                Text(
                    text = "DEV",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = PizzaTheme.ink,
                    letterSpacing = 0.08.em,
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = panelShape,
            color = PizzaTheme.surface,
            border = BorderStroke(1.dp, PizzaTheme.line),
        ) {
            Column(
                modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                STATUSES.forEach { status ->
                    StatusRow(
                        label = status.label,
                        sub = status.sub,
                        eta = status.eta,
                        isActive = current == status.key,
                        onClick = { onSelect(status.key) },
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusRow(
    label: String,
    sub: String,
    eta: Int,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    val bg by animateColorAsState(
        targetValue = if (isActive) PizzaTheme.accentSoft else Color.Transparent,
        animationSpec = tween(150),
        label = "row-bg",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(rowShape)
            .background(bg)
            .clickable(onClick = onClick)
            .padding(PaddingValues(horizontal = 12.dp, vertical = 12.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isActive) PizzaTheme.accent else PizzaTheme.surface)
                .border(
                    width = if (isActive) 0.dp else 1.5.dp,
                    color = if (isActive) Color.Transparent else PizzaTheme.line,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (isActive) {
                Text(
                    text = "✓",
                    color = PizzaTheme.accentInk,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                color = PizzaTheme.ink,
                letterSpacing = (-0.01).em,
            )
            Text(
                text = if (eta > 0) "$sub · $eta min" else sub,
                fontSize = 11.5.sp,
                fontFamily = bodyFont,
                color = PizzaTheme.sub,
            )
        }

        AnimatedVisibility(
            visible = isActive,
            enter = fadeIn(tween(150)) + scaleIn(tween(150), initialScale = 0.85f),
            exit = fadeOut(tween(120)) + scaleOut(tween(120), targetScale = 0.85f),
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = PizzaTheme.accent,
            ) {
                Text(
                    text = "NOW",
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    color = PizzaTheme.accentInk,
                    fontSize = 9.5.sp,
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.08.em,
                )
            }
        }
    }
}

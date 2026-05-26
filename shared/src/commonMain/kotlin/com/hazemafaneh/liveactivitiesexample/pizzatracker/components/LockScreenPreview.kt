package com.hazemafaneh.liveactivitiesexample.pizzatracker.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hazemafaneh.liveactivitiesexample.pizzatracker.OrderInfo
import com.hazemafaneh.liveactivitiesexample.pizzatracker.PizzaTheme
import com.hazemafaneh.liveactivitiesexample.pizzatracker.StatusKey
import com.hazemafaneh.liveactivitiesexample.pizzatracker.bodyFont
import com.hazemafaneh.liveactivitiesexample.pizzatracker.displayFont
import com.hazemafaneh.liveactivitiesexample.pizzatracker.statusOf

@Composable
fun LockScreenPreview(
    current: StatusKey,
    order: OrderInfo,
    modifier: Modifier = Modifier,
) {
    val status = statusOf(current)
    val progress by animateFloatAsState(
        targetValue = status.pct / 100f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "lock-progress",
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionHeader(label = "LOCK SCREEN PREVIEW")

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color(0xEB141416),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PizzaTheme.accent),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "🍕",
                            fontSize = 18.sp,
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                    ) {
                        Text(
                            text = order.vendor,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = bodyFont,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = status.label,
                            color = Color.White.copy(alpha = 0.72f),
                            fontSize = 11.sp,
                            fontFamily = bodyFont,
                        )
                    }

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = if (status.eta > 0) status.eta.toString() else "—",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontFamily = displayFont,
                            letterSpacing = (-0.03).em,
                        )
                        Text(
                            text = if (status.eta > 0) " min" else "",
                            color = Color.White.copy(alpha = 0.72f),
                            fontSize = 11.sp,
                            fontFamily = bodyFont,
                            modifier = Modifier.padding(bottom = 4.dp, start = 2.dp),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.12f)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        PizzaTheme.accent,
                                        PizzaTheme.pizzaYellow,
                                    ),
                                ),
                            ),
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    LockMeta("ORDER", order.id)
                    LockMeta("ITEM", order.itemHeadline, align = Alignment.CenterHorizontally)
                    LockMeta(
                        label = "STATUS",
                        value = "${status.pct}%",
                        align = Alignment.End,
                    )
                }
            }
        }
    }
}

@Composable
private fun LockMeta(
    label: String,
    value: String,
    align: Alignment.Horizontal = Alignment.Start,
) {
    Column(horizontalAlignment = align) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 9.sp,
            fontFamily = bodyFont,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.em,
        )
        Text(
            text = value,
            color = Color.White.copy(alpha = 0.92f),
            fontSize = 11.5.sp,
            fontFamily = bodyFont,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun SectionHeader(label: String, trailing: (@Composable () -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontFamily = bodyFont,
            fontWeight = FontWeight.SemiBold,
            color = PizzaTheme.sub,
            letterSpacing = 0.08.em,
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = PizzaTheme.line,
        )
        trailing?.invoke()
    }
}

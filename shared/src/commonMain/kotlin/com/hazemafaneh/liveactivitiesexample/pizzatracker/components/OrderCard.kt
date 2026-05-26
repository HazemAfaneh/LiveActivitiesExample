package com.hazemafaneh.liveactivitiesexample.pizzatracker.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hazemafaneh.liveactivitiesexample.pizzatracker.OrderInfo
import com.hazemafaneh.liveactivitiesexample.pizzatracker.PizzaTheme
import com.hazemafaneh.liveactivitiesexample.pizzatracker.StatusKey
import com.hazemafaneh.liveactivitiesexample.pizzatracker.bodyFont
import com.hazemafaneh.liveactivitiesexample.pizzatracker.cardShape
import com.hazemafaneh.liveactivitiesexample.pizzatracker.displayFont
import com.hazemafaneh.liveactivitiesexample.pizzatracker.etaDisplay
import com.hazemafaneh.liveactivitiesexample.pizzatracker.pillShape
import com.hazemafaneh.liveactivitiesexample.pizzatracker.statusOf

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OrderCard(
    current: StatusKey,
    order: OrderInfo,
    modifier: Modifier = Modifier,
) {
    val status = statusOf(current)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = cardShape,
                spotColor = Color.Black.copy(alpha = 0.18f),
                ambientColor = Color.Black.copy(alpha = 0.10f),
            ),
        shape = cardShape,
        color = PizzaTheme.surface,
        border = BorderStroke(1.dp, PizzaTheme.line),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CardTopRow(order = order)

            EtaHeroRow(
                etaMinutes = status.eta,
                statusLabel = status.label,
                statusSub = status.sub,
            )

            ProgressStepper(current = current)
        }
    }
}

@Composable
private fun CardTopRow(order: OrderInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PizzaIconMark()

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "${order.vendor.uppercase()} · ${order.id}",
                fontSize = 10.5.sp,
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                color = PizzaTheme.sub,
                letterSpacing = 0.06.em,
            )
            Text(
                text = order.itemHeadline,
                fontSize = 15.sp,
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                color = PizzaTheme.ink,
                letterSpacing = (-0.01).em,
            )
        }

        Surface(
            shape = pillShape,
            color = PizzaTheme.accentSoft,
        ) {
            Row(
                modifier = Modifier.padding(PaddingValues(horizontal = 10.dp, vertical = 5.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                PingDot(color = PizzaTheme.accent, dotSize = 6f, boxSize = 12f)
                Text(
                    text = "LIVE",
                    color = PizzaTheme.accent,
                    fontSize = 11.sp,
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.06.em,
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun EtaHeroRow(
    etaMinutes: Int,
    statusLabel: String,
    statusSub: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "ETA",
                fontSize = 11.sp,
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                color = PizzaTheme.sub,
                letterSpacing = 0.06.em,
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                AnimatedContent(
                    targetState = etaMinutes,
                    transitionSpec = {
                        if (targetState < initialState) {
                            (slideInVertically { it } + fadeIn()) togetherWith
                                (slideOutVertically { -it } + fadeOut())
                        } else {
                            (slideInVertically { -it } + fadeIn()) togetherWith
                                (slideOutVertically { it } + fadeOut())
                        }
                    },
                    label = "eta",
                ) { eta ->
                    Text(
                        text = if (eta > 0) eta.toString() else "—",
                        style = etaDisplay,
                        color = PizzaTheme.ink,
                        fontFamily = displayFont,
                    )
                }
                Text(
                    text = if (etaMinutes > 0) "min" else "",
                    fontSize = 14.sp,
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.Medium,
                    color = PizzaTheme.sub,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = statusLabel,
                fontSize = 15.sp,
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                color = PizzaTheme.ink,
                letterSpacing = (-0.01).em,
            )
            Text(
                text = statusSub,
                fontSize = 12.sp,
                fontFamily = bodyFont,
                color = PizzaTheme.sub,
            )
        }
    }
}

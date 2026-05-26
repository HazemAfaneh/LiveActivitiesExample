package com.hazemafaneh.liveactivitiesexample.pizzatracker.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazemafaneh.liveactivitiesexample.pizzatracker.PizzaTheme
import com.hazemafaneh.liveactivitiesexample.pizzatracker.STATUSES
import com.hazemafaneh.liveactivitiesexample.pizzatracker.StatusKey
import com.hazemafaneh.liveactivitiesexample.pizzatracker.bodyFont
import com.hazemafaneh.liveactivitiesexample.pizzatracker.statusIndex

private val DOT_SIZE = 22.dp

@Composable
fun ProgressStepper(
    current: StatusKey,
    modifier: Modifier = Modifier,
) {
    val activeIndex = statusIndex(current).coerceAtLeast(0)
    val target = activeIndex / (STATUSES.size - 1).toFloat()
    val progress by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "stepper-progress",
    )

    Box(modifier = modifier.fillMaxWidth().height(54.dp)) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(DOT_SIZE)
                .align(Alignment.TopCenter),
        ) {
            val y = size.height / 2f
            val pad = (DOT_SIZE / 2).toPx()
            val stroke = 3.dp.toPx()

            drawLine(
                color = PizzaTheme.line,
                start = Offset(pad, y),
                end = Offset(size.width - pad, y),
                strokeWidth = stroke,
                cap = StrokeCap.Round,
            )
            val fillEnd = pad + (size.width - 2 * pad) * progress
            if (fillEnd > pad) {
                drawLine(
                    color = PizzaTheme.accent,
                    start = Offset(pad, y),
                    end = Offset(fillEnd, y),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            STATUSES.forEachIndexed { index, status ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    StepDot(
                        state = when {
                            index < activeIndex -> StepState.Done
                            index == activeIndex -> StepState.Current
                            else -> StepState.Future
                        },
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = status.label,
                        fontSize = 10.5.sp,
                        fontFamily = bodyFont,
                        fontWeight = if (index == activeIndex) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (index <= activeIndex) PizzaTheme.ink else PizzaTheme.sub,
                    )
                }
            }
        }
    }
}

private enum class StepState { Done, Current, Future }

@Composable
private fun StepDot(state: StepState) {
    when (state) {
        StepState.Done -> Box(
            modifier = Modifier
                .size(DOT_SIZE)
                .clip(CircleShape)
                .background(PizzaTheme.accent),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "✓",
                color = PizzaTheme.accentInk,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        StepState.Current -> Box(
            modifier = Modifier
                .size(DOT_SIZE)
                .border(5.dp, PizzaTheme.accentSoft, CircleShape)
                .clip(CircleShape)
                .background(PizzaTheme.accent),
        )

        StepState.Future -> Box(
            modifier = Modifier
                .size(DOT_SIZE)
                .clip(CircleShape)
                .background(PizzaTheme.surface)
                .border(2.dp, PizzaTheme.line, CircleShape),
        )
    }
}

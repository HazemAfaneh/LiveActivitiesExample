package com.hazemafaneh.liveactivitiesexample.pizzatracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hazemafaneh.liveactivitiesexample.DeliveryAttributes
import com.hazemafaneh.liveactivitiesexample.DeliveryState
import com.hazemafaneh.liveactivitiesexample.pizzatracker.components.ActionButton
import com.hazemafaneh.liveactivitiesexample.pizzatracker.components.DevStatusPanel
import com.hazemafaneh.liveactivitiesexample.pizzatracker.components.EmptyState
import com.hazemafaneh.liveactivitiesexample.pizzatracker.components.LockScreenPreview
import com.hazemafaneh.liveactivitiesexample.pizzatracker.components.OrderCard
import io.github.hazemafaneh.liveactivities.DismissalPolicy
import io.github.hazemafaneh.liveactivities.LiveActivity
import io.github.hazemafaneh.liveactivities.LiveActivityConfig
import io.github.hazemafaneh.liveactivities.LiveActivityManager
import io.github.hazemafaneh.liveactivities.PushType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours
import com.hazemafaneh.liveactivitiesexample.DeliveryStatus as SdkStatus

private enum class Section {
    OrderCard, EmptyState, LockPreview, Action, DevPanel
}

private typealias DeliveryHandle = LiveActivity<DeliveryAttributes, DeliveryState>

@Composable
fun PizzaTrackerScreen() {
    var currentStatus by remember { mutableStateOf(StatusKey.ARRIVING) }
    var hasOrder by remember { mutableStateOf(true) }
    val order = remember { OrderInfo() }

    var handle by remember { mutableStateOf<DeliveryHandle?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (hasOrder && handle == null) {
            startActivity(order, currentStatus) { handle = it }
        }
    }

    PizzaTheme {
        Scaffold(
            containerColor = PizzaTheme.bg,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { _ ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(items = Section.entries, key = { it.name }) { section ->
                when (section) {
                    Section.OrderCard -> AnimatedVisibility(
                        visible = hasOrder,
                        enter = fadeIn(tween(220)) + expandVertically(tween(220)),
                        exit = fadeOut(tween(180)) + shrinkVertically(tween(180)),
                    ) {
                        OrderCard(
                            current = currentStatus,
                            order = order,
                            modifier = Modifier.statusBarsPadding(),
                        )
                    }

                    Section.EmptyState -> AnimatedVisibility(
                        visible = !hasOrder,
                        enter = fadeIn(tween(220)) + expandVertically(tween(220)),
                        exit = fadeOut(tween(180)) + shrinkVertically(tween(180)),
                    ) {
                        EmptyState(modifier = Modifier.statusBarsPadding())
                    }

                    Section.LockPreview -> LockScreenPreview(
                        current = if (hasOrder) currentStatus else StatusKey.PREPARING,
                        order = order,
                    )

                    Section.Action -> ActionButton(
                        hasOrder = hasOrder,
                        onClick = {
                            if (hasOrder) {
                                val h = handle
                                hasOrder = false
                                currentStatus = StatusKey.PREPARING
                                handle = null
                                if (h != null) {
                                    scope.launch {
                                        LiveActivityManager.end(h.id, DismissalPolicy.Immediate)
                                    }
                                }
                            } else {
                                hasOrder = true
                                currentStatus = StatusKey.PREPARING
                                scope.launch {
                                    startActivity(order, StatusKey.PREPARING) { handle = it }
                                }
                            }
                        },
                    )

                    Section.DevPanel -> AnimatedVisibility(
                        visible = hasOrder,
                        enter = fadeIn(tween(220)) + expandVertically(tween(220)),
                        exit = fadeOut(tween(180)) + shrinkVertically(tween(180)),
                    ) {
                        DevStatusPanel(
                            current = currentStatus,
                            onSelect = { key ->
                                currentStatus = key
                                pushStatus(scope, handle, order, key) { handle = it }
                            },
                        )
                    }
                }
            }

            item{
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        }
    }
}

private fun pushStatus(
    scope: CoroutineScope,
    handle: DeliveryHandle?,
    order: OrderInfo,
    key: StatusKey,
    onStarted: (DeliveryHandle) -> Unit,
) {
    val state = stateFor(key)
    if (handle != null) {
        scope.launch {
            LiveActivityManager.update(handle.id, state)
        }
    } else {
        scope.launch {
            startActivity(order, key, onStarted)
        }
    }
}

private suspend fun startActivity(
    order: OrderInfo,
    key: StatusKey,
    onStarted: (DeliveryHandle) -> Unit,
) {
    LiveActivityManager.start(
        attributes = DeliveryAttributes(order.id, order.vendor, order.itemHeadline),
        initialState = stateFor(key),
        config = LiveActivityConfig(
            pushType = PushType.Token,
            staleAfter = 1.hours,
        ),
    ).onSuccess(onStarted)
}

private fun stateFor(key: StatusKey): DeliveryState {
    val status = statusOf(key)
    val driver = if (key != StatusKey.PREPARING) "Ahmad" else null
    val sdkStatus = when (key) {
        StatusKey.PREPARING -> SdkStatus.PREPARING
        StatusKey.ON_WAY    -> SdkStatus.ON_THE_WAY
        StatusKey.ARRIVING  -> SdkStatus.ARRIVING
        StatusKey.DELIVERED -> SdkStatus.DELIVERED
    }
    return DeliveryState(
        status = sdkStatus,
        etaMinutes = status.eta,
        driverName = driver,
    )
}

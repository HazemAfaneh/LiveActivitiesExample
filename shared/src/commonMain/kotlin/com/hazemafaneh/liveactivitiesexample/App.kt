package com.hazemafaneh.liveactivitiesexample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.hazemafaneh.liveactivities.DismissalPolicy
import io.github.hazemafaneh.liveactivities.LiveActivity
import io.github.hazemafaneh.liveactivities.LiveActivityConfig
import io.github.hazemafaneh.liveactivities.LiveActivityManager
import io.github.hazemafaneh.liveactivities.PushType
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours

private const val ORDER_ID = "PIZZA-12345"
private const val RESTAURANT = "Demo Pizzeria"

@Composable
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val scroll = rememberScrollState()
        var handle by remember { mutableStateOf<LiveActivity<DeliveryAttributes, DeliveryState>?>(null) }
        var error by remember { mutableStateOf<String?>(null) }

        val activities by LiveActivityManager.activities.collectAsState()
        val enabled by LiveActivityManager.areActivitiesEnabled.collectAsState()

        val current = handle
        val token by (current?.pushToken?.collectAsState() ?: remember { mutableStateOf(null) })
        val state by (current?.state?.collectAsState() ?: remember { mutableStateOf(null) })

        LaunchedEffect(token) {
            token?.let { println("Live Activity push token: $it") }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .verticalScroll(scroll)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Pizza Delivery Tracker",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Live Activities enabled: $enabled · tracked: ${activities.size}",
                style = MaterialTheme.typography.bodyMedium,
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("Order $ORDER_ID · $RESTAURANT", fontWeight = FontWeight.Medium)
                    state?.let { s ->
                        Text(s.headline())
                        AssistChip(
                            onClick = {},
                            label = { Text("${s.progressPercent()}%") },
                            colors = AssistChipDefaults.assistChipColors(),
                        )
                    } ?: Text("No activity yet — tap Start Order.")
                }
            }

            Button(
                onClick = {
                    error = null
                    scope.launch {
                        val result = LiveActivityManager.start(
                            attributes = DeliveryAttributes(ORDER_ID, RESTAURANT),
                            initialState = DeliveryState(DeliveryStatus.PREPARING, etaMinutes = 30),
                            config = LiveActivityConfig(
                                pushType = PushType.Token,
                                staleAfter = 1.hours,
                            ),
                        )
                        result.onSuccess { handle = it }
                            .onFailure { error = it.message ?: it::class.simpleName }
                    }
                },
                enabled = handle == null,
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Start Order") }

            Spacer(Modifier.height(4.dp))
            Text("Update status", style = MaterialTheme.typography.titleMedium)

            UpdateButton("Preparing · 30 min", handle) {
                DeliveryState(DeliveryStatus.PREPARING, etaMinutes = 30)
            }
            UpdateButton("On the way · 12 min · Ahmad", handle) {
                DeliveryState(DeliveryStatus.ON_THE_WAY, etaMinutes = 12, driverName = "Ahmad")
            }
            UpdateButton("Arriving · 3 min", handle) {
                DeliveryState(DeliveryStatus.ARRIVING, etaMinutes = 3, driverName = "Ahmad")
            }
            UpdateButton("Delivered", handle) {
                DeliveryState(DeliveryStatus.DELIVERED, etaMinutes = 0, driverName = "Ahmad")
            }

            OutlinedButton(
                onClick = {
                    val id = handle?.id ?: return@OutlinedButton
                    scope.launch {
                        LiveActivityManager.end(id, DismissalPolicy.Immediate)
                            .onSuccess { handle = null }
                            .onFailure { error = it.message ?: it::class.simpleName }
                    }
                },
                enabled = handle != null,
                modifier = Modifier.fillMaxWidth(),
            ) { Text("End") }

            error?.let {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Error: $it",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Push token (logcat / Xcode console): ${token ?: "—"}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun UpdateButton(
    label: String,
    handle: LiveActivity<DeliveryAttributes, DeliveryState>?,
    state: () -> DeliveryState,
) {
    val scope = rememberCoroutineScope()
    OutlinedButton(
        onClick = {
            val id = handle?.id ?: return@OutlinedButton
            scope.launch { LiveActivityManager.update(id, state()) }
        },
        enabled = handle != null,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(horizontalAlignment = Alignment.Start) { Text(label) }
    }
}

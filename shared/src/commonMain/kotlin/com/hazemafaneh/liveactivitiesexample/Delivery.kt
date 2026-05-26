package com.hazemafaneh.liveactivitiesexample

import io.github.hazemafaneh.liveactivities.LiveActivityAttributes
import io.github.hazemafaneh.liveactivities.LiveActivityContentState
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryAttributes(
    val orderId: String,
    val restaurantName: String,
) : LiveActivityAttributes

@Serializable
data class DeliveryState(
    val status: String,
    val etaMinutes: Int,
    val driverName: String? = null,
) : LiveActivityContentState

object DeliveryStatus {
    const val PREPARING = "preparing"
    const val ON_THE_WAY = "on_the_way"
    const val ARRIVING = "arriving"
    const val DELIVERED = "delivered"
}

fun DeliveryState.headline(): String = when (status) {
    DeliveryStatus.PREPARING -> "Preparing your order"
    DeliveryStatus.ON_THE_WAY -> {
        val driver = driverName?.let { " · $it" }.orEmpty()
        "On the way · ETA $etaMinutes min$driver"
    }
    DeliveryStatus.ARRIVING -> "Arriving · ETA $etaMinutes min"
    DeliveryStatus.DELIVERED -> "Delivered — enjoy!"
    else -> status
}

fun DeliveryState.progressPercent(): Int = when (status) {
    DeliveryStatus.PREPARING -> 10
    DeliveryStatus.ON_THE_WAY -> 50
    DeliveryStatus.ARRIVING -> 85
    DeliveryStatus.DELIVERED -> 100
    else -> 0
}

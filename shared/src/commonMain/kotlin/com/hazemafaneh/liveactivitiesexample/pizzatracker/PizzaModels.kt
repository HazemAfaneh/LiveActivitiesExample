package com.hazemafaneh.liveactivitiesexample.pizzatracker

enum class StatusKey { PREPARING, ON_WAY, ARRIVING, DELIVERED }

data class DeliveryStatus(
    val key: StatusKey,
    val label: String,
    val eta: Int,
    val sub: String,
    val pct: Int,
)

val STATUSES = listOf(
    DeliveryStatus(StatusKey.PREPARING, "Preparing",  30, "In the oven",         25),
    DeliveryStatus(StatusKey.ON_WAY,    "On the way", 12, "Ahmad · Honda CB300", 60),
    DeliveryStatus(StatusKey.ARRIVING,  "Arriving",    3, "Around the corner",   85),
    DeliveryStatus(StatusKey.DELIVERED, "Delivered",   0, "Enjoy",              100),
)

fun statusOf(key: StatusKey): DeliveryStatus = STATUSES.first { it.key == key }

fun statusIndex(key: StatusKey): Int = STATUSES.indexOfFirst { it.key == key }

data class OrderInfo(
    val id: String = "PIZZA-12345",
    val vendor: String = "Pizza Hut",
    val itemHeadline: String = "1× Large Pepperoni",
)

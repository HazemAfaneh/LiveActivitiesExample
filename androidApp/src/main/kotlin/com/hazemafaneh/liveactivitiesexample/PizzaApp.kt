package com.hazemafaneh.liveactivitiesexample

import android.app.Application
import android.graphics.Color
import io.github.hazemafaneh.liveactivities.DefaultProgressRenderer
import io.github.hazemafaneh.liveactivities.LiveActivityManager
import io.github.hazemafaneh.liveactivities.ProgressStyleData
import io.github.hazemafaneh.liveactivities.StatusChipConfig

class PizzaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        LiveActivityManager.init(this)
        LiveActivityManager.registerRenderer(
            DeliveryState::class,
            DefaultProgressRenderer(
                title = { "$RESTAURANT · order $ORDER_ID" },
                text = { it.headline() },
                progressStyle = { state -> buildProgressStyle(state) },
                subText = { state ->
                    state.driverName?.takeIf { state.status != DeliveryStatus.DELIVERED }
                },
                statusChip = { state -> buildStatusChip(state) },
            ),
        )
    }

    /**
     * Builds the three-segment delivery progress: preparing → on the way → delivered.
     *
     * Three equal-length segments at 33/33/34 sum to 100. Points sit at the segment boundaries
     * so the user can see where each stage starts. The tracker icon follows live progress.
     */
    private fun buildProgressStyle(state: DeliveryState): ProgressStyleData =
        ProgressStyleData(
            progress = state.progressPercent(),
            segments = listOf(
                ProgressStyleData.Segment(length = 33, color = COLOR_PREPARING),
                ProgressStyleData.Segment(length = 33, color = COLOR_ON_THE_WAY),
                ProgressStyleData.Segment(length = 34, color = COLOR_DELIVERED),
            ),
            points = listOf(
                ProgressStyleData.Point(position = 33, color = COLOR_POINT),
                ProgressStyleData.Point(position = 66, color = COLOR_POINT),
            ),
            startIconResId = R.drawable.ic_progress_start,
            endIconResId = R.drawable.ic_progress_end,
            trackerIconResId = R.drawable.ic_progress_tracker,
        )

    /** Live-updating status-bar chip — short ETA on early stages, "Done" once delivered. */
    private fun buildStatusChip(state: DeliveryState): StatusChipConfig =
        if (state.status == DeliveryStatus.DELIVERED) {
            StatusChipConfig.CriticalText("Done")
        } else {
            // Keep within the chip's 7-char render budget so it doesn't truncate.
            StatusChipConfig.CriticalText("${state.etaMinutes}m")
        }

    private companion object {
        const val ORDER_ID = "PIZZA-12345"
        const val RESTAURANT = "Demo Pizzeria"

        // Distinct hues so each delivery stage is visually identifiable.
        val COLOR_PREPARING: Int = Color.parseColor("#FFB300") // amber
        val COLOR_ON_THE_WAY: Int = Color.parseColor("#1E88E5") // blue
        val COLOR_DELIVERED: Int = Color.parseColor("#43A047") // green
        val COLOR_POINT: Int = Color.parseColor("#FFFFFF")
    }
}

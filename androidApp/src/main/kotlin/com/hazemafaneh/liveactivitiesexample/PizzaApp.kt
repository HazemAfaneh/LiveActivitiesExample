package com.hazemafaneh.liveactivitiesexample

import android.app.Application
import android.graphics.Color
import io.github.hazemafaneh.liveactivities.AttributedLiveActivityRenderer
import io.github.hazemafaneh.liveactivities.LiveActivityManager
import io.github.hazemafaneh.liveactivities.LiveActivityNotificationContent
import io.github.hazemafaneh.liveactivities.ProgressStyleData
import io.github.hazemafaneh.liveactivities.StatusChipConfig

/**
 * Wires the Live Activity renderer to mirror the in-app `LockScreenPreview` from
 * `PizzaTrackerScreen`:
 *
 *  - Title       → vendor name from attributes ("Pizza Hut")
 *  - Text        → human status label ("Preparing", "On the way", ...)
 *  - SubText     → item headline from attributes ("1× Large Pepperoni")
 *  - AccentColor → the screen's accent red, tinting app name + small icon
 *  - Progress    → 4 stage segments that recolour as the order advances, mimicking the
 *                  accent-to-yellow gradient bar shown in the preview
 *  - StatusChip  → live ETA in the status-bar pill, "Done" once delivered
 */
class PizzaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        LiveActivityManager.init(this)
        LiveActivityManager.registerAttributedRenderer(
            stateType = DeliveryState::class,
            renderer = AttributedLiveActivityRenderer<DeliveryAttributes, DeliveryState> { attributes, state ->
                LiveActivityNotificationContent(
                    title = attributes.restaurantName,
                    text = statusLabel(state),
                    subText = "${attributes.itemHeadline} · ${attributes.orderId}",
                    accentColor = ACCENT,
                    progressStyle = buildProgressStyle(state),
                    statusChip = buildStatusChip(state),
                )
            },
        )
    }


    private fun statusLabel(state: DeliveryState): String = when (state.status) {
        DeliveryStatus.PREPARING -> "Preparing"
        DeliveryStatus.ON_THE_WAY -> "On the way · ${state.driverName ?: "Driver assigned"}"
        DeliveryStatus.ARRIVING -> "Arriving · around the corner"
        DeliveryStatus.DELIVERED -> "Delivered — enjoy"
        else -> state.status
    }

    /**
     * Four equal-length segments at 25/25/25/25 mirror the four stages shown in the in-app
     * stepper. Each segment recolours from neutral track to a warm accent → yellow gradient
     * stop as `progress` crosses it, giving the bar the same visual "fill" the preview shows.
     */
    private fun buildProgressStyle(state: DeliveryState): ProgressStyleData {
        val pct = state.progressPercent()
        return ProgressStyleData(
            progress = pct,
            segments = listOf(
                stageSegment(threshold = 25, color = SEG_ACCENT_DEEP, pct = pct),
                stageSegment(threshold = 50, color = SEG_ACCENT, pct = pct),
                stageSegment(threshold = 85, color = SEG_AMBER, pct = pct),
                stageSegment(threshold = 100, color = SEG_YELLOW, pct = pct),
            ),
            points = listOf(
                ProgressStyleData.Point(position = 25, color = POINT_COLOR),
                ProgressStyleData.Point(position = 50, color = POINT_COLOR),
                ProgressStyleData.Point(position = 85, color = POINT_COLOR),
            ),
            startIconResId = R.drawable.ic_progress_start,
            endIconResId = R.drawable.ic_progress_end,
            trackerIconResId = R.drawable.ic_progress_tracker,
        )
    }

    private fun stageSegment(threshold: Int, color: Int, pct: Int): ProgressStyleData.Segment {
        // Reached stages keep their colour; future stages dim down to a faint track tint so the
        // active gradient stands out the way it does in the in-app preview.
        val tinted = if (pct >= threshold) color else SEG_TRACK
        return ProgressStyleData.Segment(length = 25, color = tinted)
    }

    private fun buildStatusChip(state: DeliveryState): StatusChipConfig =
        if (state.status == DeliveryStatus.DELIVERED) {
            StatusChipConfig.CriticalText("Done")
        } else {
            StatusChipConfig.CriticalText("${state.etaMinutes}m")
        }

    private companion object {
        // Matches PizzaTheme.accent from the screen.
        val ACCENT: Int = Color.parseColor("#E03A2F")

        // Gradient-style stops blending accent red into pizza yellow.
        val SEG_ACCENT_DEEP: Int = Color.parseColor("#C2261A")
        val SEG_ACCENT: Int = Color.parseColor("#E03A2F")
        val SEG_AMBER: Int = Color.parseColor("#F08148")
        val SEG_YELLOW: Int = Color.parseColor("#F4C152")
        val SEG_TRACK: Int = Color.parseColor("#1F1A1410") // 12% ink

        val POINT_COLOR: Int = Color.parseColor("#FFFFFF")
    }
}

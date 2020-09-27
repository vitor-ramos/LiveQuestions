package dev.vitorramos.livequestions.composables

import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.ExponentialDecay
import androidx.compose.animation.core.TargetAnimation
import androidx.compose.foundation.animation.FlingConfig
import androidx.compose.foundation.gestures.ScrollableController
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.unit.dp
import dev.vitorramos.livequestions.model.Question
import dev.vitorramos.livequestions.model.SiteData
import kotlin.math.abs
import kotlin.math.roundToInt

interface BodyContentEvents {
    fun onLoadNextPage()
}

@Composable
fun BodyContent(
    questions: List<Question>,
    site: SiteData,
) {
    WithConstraints {
        var offset by remember { mutableStateOf(0f) }
        val consumeScrollDelta: (Float) -> Float = {
            offset += it
            it
        }
        var index by remember { mutableStateOf(0) }
        val roundedOffset = offset.roundToInt().toFloat()
        var transitioning = false
        if (roundedOffset % constraints.maxWidth == 0f) {
            val aux = (-roundedOffset / constraints.maxWidth).toInt()
            if (aux != index) {
                transitioning = true
                index = aux
            }
        }

        val flingConfig = FlingConfig(ExponentialDecay()) {
            adjustTarget(
                it,
                index,
                questions.size / 2,
                constraints.maxWidth
            )
        }
        val clocks = AmbientAnimationClock.current.asDisposableClock()
        val scrollableController = remember(questions) {
            ScrollableController(consumeScrollDelta, flingConfig, clocks)
        }
        Column(
            modifier = Modifier.scrollable(
                orientation = Orientation.Horizontal,
                controller = scrollableController,
            ),
        ) {
            val availableHeight = AmbientConfiguration.current.screenHeightDp.dp - 112.dp

            if (index in questions.indices) {
                QuestionCard(
                    questions,
                    Modifier.height(availableHeight / 2),
                    ChipStyling(site),
                    offset,
                    constraints.maxWidth,
                    index,
                    0,
                    2,
                    transitioning,
                )
            }
            val bottomIndexOffset = 1
            if (index + bottomIndexOffset in questions.indices) {
                Divider(color = Color.Black)
                QuestionCard(
                    questions,
                    Modifier.height(availableHeight / 2),
                    ChipStyling(site),
                    offset,
                    constraints.maxWidth,
                    index,
                    bottomIndexOffset,
                    2,
                    transitioning,
                )
            }
        }
    }
}

private fun adjustTarget(target: Float, index: Int, size: Int, width: Int): TargetAnimation {
    var result = width * -1 * index
    if (index > 0) {
        val previous = width * -1 * (index - 1)
        if (abs(previous - target) < abs(result - target)) result = previous
    }
    if (index < size) {
        val next = width * -1 * (index + 1)
        if (abs(next - target) < abs(result - target)) result = next
    }
    return TargetAnimation(result.toFloat())
}

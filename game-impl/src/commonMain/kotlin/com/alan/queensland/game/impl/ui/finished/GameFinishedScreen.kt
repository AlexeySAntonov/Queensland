package com.alan.queensland.game.impl.ui.finished

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun GameFinishedScreen(
    viewModel: GameFinishedViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        CelebrationConfetti(modifier = Modifier.fillMaxSize())
        VictoryContent(modifier = Modifier.align(Alignment.Center))
        IconButton(
            onClick = viewModel::onCloseClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(Paddings.half),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
            )
        }
    }
}

@Composable
private fun VictoryContent(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Victory animation")
    val starScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Victory star scale",
    )
    val starRotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1_200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Victory star rotation",
    )

    Column(
        modifier = modifier.padding(horizontal = Paddings.two),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier
                .size(128.dp)
                .graphicsLayer {
                    scaleX = starScale
                    scaleY = starScale
                    rotationZ = starRotation
                },
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.height(Paddings.two))
        Text(
            text = "Puzzle solved!",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(Paddings.half))
        Text(
            text = "Every queen is safe.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CelebrationConfetti(
    modifier: Modifier = Modifier,
) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error,
    )
    val infiniteTransition = rememberInfiniteTransition(label = "Confetti animation")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4_800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Confetti progress",
    )

    Canvas(modifier = modifier) {
        val particleWidth = 7.dp.toPx()
        val particleHeight = 16.dp.toPx()
        val swayDistance = 24.dp.toPx()

        repeat(CONFETTI_COUNT) { index ->
            val startOffset = ((index * 29) % CONFETTI_COUNT) / CONFETTI_COUNT.toFloat()
            val travelProgress = (progress + startOffset) % 1f
            val baseX = ((index * 47) % 101) / 100f * size.width
            val sway = sin(travelProgress * PI * 4 + index).toFloat() * swayDistance
            val center = Offset(
                x = (baseX + sway).coerceIn(0f, size.width),
                y = travelProgress * (size.height + particleHeight * 2) - particleHeight,
            )
            val width = if (index % 3 == 0) particleWidth * 1.5f else particleWidth
            val height = if (index % 4 == 0) particleHeight * 0.65f else particleHeight

            rotate(
                degrees = (travelProgress * 720f + index * 31f) % 360f,
                pivot = center,
            ) {
                drawRect(
                    color = colors[index % colors.size],
                    topLeft = Offset(center.x - width / 2, center.y - height / 2),
                    size = Size(width = width, height = height),
                )
            }
        }
    }
}

private const val CONFETTI_COUNT = 44

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import org.jetbrains.compose.resources.stringResource
import queensland.game_impl.generated.resources.Res
import queensland.game_impl.generated.resources.game_finished_message
import queensland.game_impl.generated.resources.game_finished_title
import queensland.game_impl.generated.resources.navigation_close
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun GameFinishedScreen(
    viewModel: GameFinishedViewModel,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CelebrationConfetti(modifier = Modifier.fillMaxSize())
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
            ) {
                VictoryContent(modifier = Modifier.align(Alignment.Center))
                IconButton(
                    onClick = viewModel::onCloseClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(Paddings.half),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.navigation_close),
                    )
                }
            }
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
            text = stringResource(Res.string.game_finished_title),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(Paddings.half))
        Text(
            text = stringResource(Res.string.game_finished_message),
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
    val animationProgress = rememberInfiniteTransition(label = "Confetti animation")
        .animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = CONFETTI_FALL_DURATION_MILLIS, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Confetti progress",
        )

    Canvas(modifier = modifier) {
        val baseParticleSize = Size(
            width = 7.dp.toPx(),
            height = 16.dp.toPx(),
        )
        val swayDistance = 24.dp.toPx()

        confettiParticleSpecs.forEach { particle ->
            drawConfettiParticle(
                particle = particle,
                animationProgress = animationProgress.value,
                color = colors[particle.colorIndex % colors.size],
                baseParticleSize = baseParticleSize,
                swayDistance = swayDistance,
            )
        }
    }
}

private fun DrawScope.drawConfettiParticle(
    particle: ConfettiParticleSpec,
    animationProgress: Float,
    color: Color,
    baseParticleSize: Size,
    swayDistance: Float,
) {
    val travelProgress = (animationProgress + particle.startProgress) % 1f
    val appliedSwayDistance = minOf(swayDistance, size.width / 2)
    val horizontalSway = sin(
        travelProgress * CONFETTI_SWAY_RADIANS + particle.swayPhaseRadians,
    ).toFloat() * appliedSwayDistance
    val horizontalTravelDistance = size.width - appliedSwayDistance * 2
    val center = Offset(
        x = appliedSwayDistance + particle.horizontalFraction * horizontalTravelDistance + horizontalSway,
        y = travelProgress * (size.height + baseParticleSize.height * 2) - baseParticleSize.height,
    )
    val particleSize = Size(
        width = baseParticleSize.width * particle.widthMultiplier,
        height = baseParticleSize.height * particle.heightMultiplier,
    )
    val rotationDegrees = (
        travelProgress * CONFETTI_ROTATION_DEGREES + particle.rotationOffsetDegrees
    ) % FULL_ROTATION_DEGREES

    rotate(degrees = rotationDegrees, pivot = center) {
        drawRect(
            color = color,
            topLeft = Offset(
                x = center.x - particleSize.width / 2,
                y = center.y - particleSize.height / 2,
            ),
            size = particleSize,
        )
    }
}

private data class ConfettiParticleSpec(
    val startProgress: Float,
    val horizontalFraction: Float,
    val swayPhaseRadians: Double,
    val rotationOffsetDegrees: Float,
    val widthMultiplier: Float,
    val heightMultiplier: Float,
    val colorIndex: Int,
)

// Fixed offsets let one shared timeline produce a varied, deterministic particle field.
private val confettiParticleSpecs = List(CONFETTI_COUNT) { index ->
    ConfettiParticleSpec(
        startProgress = index / CONFETTI_COUNT.toFloat(),
        horizontalFraction = (index * GOLDEN_RATIO_CONJUGATE) % 1f,
        swayPhaseRadians = index.toDouble(),
        rotationOffsetDegrees = index * ROTATION_OFFSET_STEP_DEGREES,
        widthMultiplier = if (index % 3 == 0) 1.5f else 1f,
        heightMultiplier = if (index % 4 == 0) 0.65f else 1f,
        colorIndex = index,
    )
}

private const val CONFETTI_COUNT = 44
private const val CONFETTI_FALL_DURATION_MILLIS = 4_800
private const val CONFETTI_ROTATION_DEGREES = 720f
private const val FULL_ROTATION_DEGREES = 360f
private const val GOLDEN_RATIO_CONJUGATE = 0.61803395f
private const val ROTATION_OFFSET_STEP_DEGREES = 31f
private val CONFETTI_SWAY_RADIANS = PI * 4

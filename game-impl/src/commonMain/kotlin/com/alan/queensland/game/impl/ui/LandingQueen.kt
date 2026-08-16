package com.alan.queensland.game.impl.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.alan.queensland.core.ui.base.compose.components.AppQueen
import org.jetbrains.compose.resources.painterResource
import queensland.game_impl.generated.resources.Res
import queensland.game_impl.generated.resources.ic_exhaust_v2

@Composable
internal fun BoxScope.LandingQueen(
    isPlaced: Boolean,
    clipExhaustToCellBounds: Boolean,
) {
    var wasPlaced by remember { mutableStateOf(isPlaced) }
    var isLanding by remember { mutableStateOf(false) }
    val landingOffset = remember { Animatable(0f) }
    val isWaitingForLandingStart = isPlaced && !wasPlaced

    LaunchedEffect(isPlaced) {
        if (!isPlaced) {
            wasPlaced = false
            isLanding = false
            landingOffset.snapTo(0f)
            return@LaunchedEffect
        }

        if (!wasPlaced) {
            landingOffset.snapTo(LANDING_START_OFFSET)
            wasPlaced = true
            isLanding = true
            try {
                landingOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = QUEEN_LANDING_DURATION_MILLIS,
                        easing = LinearOutSlowInEasing,
                    ),
                )
            } finally {
                isLanding = false
            }
        }
    }

    // Hide the final-position frame until the effect has moved a newly placed queen above the cell.
    if (!isPlaced || isWaitingForLandingStart) return

    if (isLanding) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .then(
                    if (clipExhaustToCellBounds) Modifier.clipToBounds() else Modifier,
                )
                .landingTranslation(landingOffset.value),
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_exhaust_v2),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(EXHAUST_WIDTH_FRACTION)
                    .aspectRatio(EXHAUST_ASPECT_RATIO)
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        translationY = size.height * EXHAUST_VERTICAL_OFFSET_FRACTION
                    },
                contentScale = ContentScale.Fit,
            )
        }
    }

    AppQueen(
        modifier = Modifier
            .fillMaxSize()
            .landingTranslation(landingOffset.value),
    )
}

private fun Modifier.landingTranslation(offset: Float): Modifier = graphicsLayer {
    clip = false
    translationY = offset * size.height
}

internal const val QUEEN_LANDING_DURATION_MILLIS = 1_000

private const val LANDING_START_OFFSET = -1.1f
private const val EXHAUST_WIDTH_FRACTION = 0.72f
private const val EXHAUST_ASPECT_RATIO = 866f / 899f
private const val EXHAUST_VERTICAL_OFFSET_FRACTION = 0.63f

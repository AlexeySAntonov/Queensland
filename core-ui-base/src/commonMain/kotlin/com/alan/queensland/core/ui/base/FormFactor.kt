package com.alan.queensland.core.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

object FormFactor {

    @Composable
    fun isWideScreen(): Boolean {
        val containerWidth = LocalWindowInfo.current.containerSize.width
        return with(LocalDensity.current) { containerWidth.toDp() } >= WIDE_SCREEN_MIN_WIDTH
    }
}

private val WIDE_SCREEN_MIN_WIDTH = 600.dp

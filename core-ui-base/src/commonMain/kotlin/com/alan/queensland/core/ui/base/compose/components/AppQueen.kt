package com.alan.queensland.core.ui.base.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import queensland.core_ui_base.generated.resources.Res
import queensland.core_ui_base.generated.resources.ic_queen_white

@Composable
fun AppQueen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_queen_white),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(APP_QUEEN_SIZE_FRACTION),
            contentScale = ContentScale.Fit,
        )
    }
}

const val APP_QUEEN_SIZE_FRACTION = 0.8f

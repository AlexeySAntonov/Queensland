package com.alan.queensland.core.ui.base.compose.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AppPiece(
    label: String,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val fontSize = with(LocalDensity.current) {
            (minOf(maxWidth, maxHeight) * PIECE_LABEL_SIZE_FRACTION).toSp()
        }
        Text(
            text = label,
            maxLines = 1,
            style = TextStyle(
                color = Color.White,
                fontSize = fontSize,
                fontWeight = FontWeight.Black,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(x = 1f, y = 1f),
                    blurRadius = 3f,
                ),
            ),
        )
    }
}

private const val PIECE_LABEL_SIZE_FRACTION = 0.62f

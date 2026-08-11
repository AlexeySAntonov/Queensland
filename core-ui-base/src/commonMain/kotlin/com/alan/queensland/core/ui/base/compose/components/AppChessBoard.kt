package com.alan.queensland.core.ui.base.compose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppChessBoard(
    boardSize: Int,
    modifier: Modifier = Modifier,
    lightSquareColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    darkSquareColor: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color = MaterialTheme.colorScheme.outline,
) {
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .border(width = 1.dp, color = borderColor),
    ) {
        val squareSize = size.width / boardSize

        repeat(boardSize) { row ->
            repeat(boardSize) { column ->
                drawRect(
                    color = if ((row + column) % 2 == 0) lightSquareColor else darkSquareColor,
                    topLeft = Offset(
                        x = column * squareSize,
                        y = row * squareSize,
                    ),
                    size = Size(squareSize, squareSize),
                )
            }
        }
    }
}

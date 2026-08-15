package com.alan.queensland.core.ui.base.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AppChessBoard(
    boardSize: Int,
    modifier: Modifier = Modifier,
    lightSquareColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    darkSquareColor: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    clipCellContent: Boolean = true,
    onCellClick: ((row: Int, column: Int) -> Unit)? = null,
    cellContent: @Composable BoxScope.(row: Int, column: Int) -> Unit = { _, _ -> },
) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .drawBehind {
                val borderWidth = 1.dp.toPx()
                drawRect(
                    color = borderColor,
                    topLeft = Offset(x = borderWidth / 2f, y = borderWidth / 2f),
                    size = Size(
                        width = size.width - borderWidth,
                        height = size.height - borderWidth,
                    ),
                    style = Stroke(width = borderWidth),
                )
            }
            .padding(1.dp),
    ) {
        repeat(boardSize) { row ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                repeat(boardSize) { column ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .then(
                                if (clipCellContent) Modifier.clipToBounds() else Modifier,
                            )
                            .background(
                                color = if ((row + column) % 2 == 0) {
                                    lightSquareColor
                                } else {
                                    darkSquareColor
                                },
                            )
                            .then(
                                if (onCellClick != null) {
                                    Modifier.clickable {
                                        onCellClick(row, column)
                                    }
                                } else Modifier
                            )
                    ) {
                        cellContent(row, column)
                    }
                }
            }
        }
    }
}

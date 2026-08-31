package com.alan.queensland.game.impl.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.alan.queensland.core.ui.base.compose.components.AppChessBoard
import com.alan.queensland.game.api.BoardPosition
import org.jetbrains.compose.resources.stringResource
import queensland.game_impl.generated.resources.Res
import queensland.game_impl.generated.resources.game_board_cell_conflicting_piece
import queensland.game_impl.generated.resources.game_board_cell_empty
import queensland.game_impl.generated.resources.game_board_cell_piece
import queensland.game_impl.generated.resources.place_piece
import queensland.game_impl.generated.resources.remove_piece
import kotlin.math.sqrt

@Composable
internal fun GameBoard(
    state: GameUiState,
    onCellClick: (row: Int, column: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pieceName = state.pieceType.label()
    val pieceLabel = state.pieceType.shortLabel()

    Box(
        modifier = modifier.aspectRatio(1f),
    ) {
        AppChessBoard(
            boardSize = state.boardSize,
            modifier = Modifier.fillMaxSize(),
            clipCellContent = false,
            onCellClick = onCellClick,
            cellContentDescription = { row, column ->
                gameCellContentDescription(
                    position = BoardPosition(row = row, column = column),
                    state = state,
                    pieceName = pieceName,
                )
            },
            cellClickLabel = { row, column ->
                val position = BoardPosition(row = row, column = column)
                val actionResource = if (position in state.piecePositions) {
                    Res.string.remove_piece
                } else {
                    Res.string.place_piece
                }
                stringResource(
                    actionResource,
                    pieceName,
                )
            },
        ) { row, column ->
            PieceCell(
                position = BoardPosition(row = row, column = column),
                state = state,
                pieceLabel = pieceLabel,
            )
        }
        ConflictArrows(
            boardSize = state.boardSize,
            conflictingPairs = state.conflictingPairs,
        )
    }
}

@Composable
private fun gameCellContentDescription(
    position: BoardPosition,
    state: GameUiState,
    pieceName: String,
): String {
    val descriptionResource = when (position) {
        in state.conflictingPositions -> Res.string.game_board_cell_conflicting_piece
        else -> Res.string.game_board_cell_piece
    }
    return if (position in state.piecePositions) {
        stringResource(
            descriptionResource,
            position.row + 1,
            position.column + 1,
            pieceName,
        )
    } else {
        stringResource(
            Res.string.game_board_cell_empty,
            position.row + 1,
            position.column + 1,
        )
    }
}

@Composable
private fun BoxScope.ConflictArrows(
    boardSize: Int,
    conflictingPairs: Set<Pair<BoardPosition, BoardPosition>>,
) {
    Canvas(modifier = Modifier.matchParentSize()) {
        conflictingPairs.forEach { (firstPiece, secondPiece) ->
            drawConflictArrow(
                firstPiece = firstPiece,
                secondPiece = secondPiece,
                boardSize = boardSize,
            )
        }
    }
}

private fun DrawScope.drawConflictArrow(
    firstPiece: BoardPosition,
    secondPiece: BoardPosition,
    boardSize: Int,
) {
    val cellSize = size.minDimension / boardSize
    val firstCenter = firstPiece.centerOffset(cellSize)
    val secondCenter = secondPiece.centerOffset(cellSize)
    val deltaX = secondCenter.x - firstCenter.x
    val deltaY = secondCenter.y - firstCenter.y
    val distance = sqrt(deltaX * deltaX + deltaY * deltaY)
    if (distance == 0f) return

    val directionX = deltaX / distance
    val directionY = deltaY / distance
    val endpointInset = minOf(cellSize * ARROW_ENDPOINT_INSET_FRACTION, distance / 4f)
    val start = Offset(
        x = firstCenter.x + directionX * endpointInset,
        y = firstCenter.y + directionY * endpointInset,
    )
    val end = Offset(
        x = secondCenter.x - directionX * endpointInset,
        y = secondCenter.y - directionY * endpointInset,
    )
    val strokeWidth = maxOf(2.dp.toPx(), cellSize * ARROW_STROKE_WIDTH_FRACTION)
    val arrowHeadLength = cellSize * ARROW_HEAD_LENGTH_FRACTION
    val arrowHeadHalfWidth = cellSize * ARROW_HEAD_HALF_WIDTH_FRACTION

    drawLine(
        color = ConflictColor,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
    drawArrowHead(
        tip = end,
        directionX = directionX,
        directionY = directionY,
        length = arrowHeadLength,
        halfWidth = arrowHeadHalfWidth,
        strokeWidth = strokeWidth,
    )
    drawArrowHead(
        tip = start,
        directionX = -directionX,
        directionY = -directionY,
        length = arrowHeadLength,
        halfWidth = arrowHeadHalfWidth,
        strokeWidth = strokeWidth,
    )
}

private fun DrawScope.drawArrowHead(
    tip: Offset,
    directionX: Float,
    directionY: Float,
    length: Float,
    halfWidth: Float,
    strokeWidth: Float,
) {
    val baseX = tip.x - directionX * length
    val baseY = tip.y - directionY * length
    val perpendicularX = -directionY
    val perpendicularY = directionX

    drawLine(
        color = ConflictColor,
        start = tip,
        end = Offset(
            x = baseX + perpendicularX * halfWidth,
            y = baseY + perpendicularY * halfWidth,
        ),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = ConflictColor,
        start = tip,
        end = Offset(
            x = baseX - perpendicularX * halfWidth,
            y = baseY - perpendicularY * halfWidth,
        ),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}

private fun BoardPosition.centerOffset(cellSize: Float) = Offset(
    x = (column + 0.5f) * cellSize,
    y = (row + 0.5f) * cellSize,
)

@Composable
private fun BoxScope.PieceCell(
    position: BoardPosition,
    state: GameUiState,
    pieceLabel: String,
) {
    val isPlaced = position in state.piecePositions

    if (isPlaced && position in state.conflictingPositions) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(ConflictColor.copy(alpha = CONFLICT_CELL_ALPHA)),
        )
    }
    LandingPiece(
        pieceLabel = pieceLabel,
        isPlaced = isPlaced,
        clipExhaustToCellBounds = position.row == state.boardSize - 1,
    )
}

private val ConflictColor = Color(0xFFFF1744)

private const val CONFLICT_CELL_ALPHA = 0.72f
private const val ARROW_ENDPOINT_INSET_FRACTION = 0.3f
private const val ARROW_STROKE_WIDTH_FRACTION = 0.055f
private const val ARROW_HEAD_LENGTH_FRACTION = 0.18f
private const val ARROW_HEAD_HALF_WIDTH_FRACTION = 0.1f

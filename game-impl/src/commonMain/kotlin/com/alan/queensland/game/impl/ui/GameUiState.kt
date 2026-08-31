package com.alan.queensland.game.impl.ui

import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.api.PieceType

data class GameUiState(
    val boardSize: Int,
    val pieceType: PieceType,
    val piecePositions: Set<BoardPosition>,
    val conflictingPositions: Set<BoardPosition>,
    val conflictingPairs: Set<Pair<BoardPosition, BoardPosition>>,
    val remainingPieceCount: Int,
    val formattedTimeSpent: String,
    val isSolved: Boolean,
)

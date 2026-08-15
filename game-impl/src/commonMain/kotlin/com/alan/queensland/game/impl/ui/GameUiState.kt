package com.alan.queensland.game.impl.ui

import com.alan.queensland.game.api.BoardPosition

data class GameUiState(
    val boardSize: Int,
    val queenPositions: Set<BoardPosition>,
    val conflictingPositions: Set<BoardPosition>,
    val conflictingPairs: Set<Pair<BoardPosition, BoardPosition>>,
    val remainingQueenCount: Int,
    val formattedTimeSpent: String,
    val isSolved: Boolean,
)

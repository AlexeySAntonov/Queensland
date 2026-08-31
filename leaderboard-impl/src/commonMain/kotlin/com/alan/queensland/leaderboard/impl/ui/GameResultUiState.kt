package com.alan.queensland.leaderboard.impl.ui

import com.alan.queensland.core.db.api.model.GameResultPieceType

data class GameResultUiState(
    val uuid: String,
    val rank: Int,
    val formattedTimeSpent: String,
    val formattedCompletedAt: String,
    val pieceType: GameResultPieceType,
    val piecePositions: Set<Pair<Int, Int>>,
)

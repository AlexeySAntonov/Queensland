package com.alan.queensland.core.db.test

import com.alan.queensland.core.db.api.model.GameResultPieceType

internal data class SavedGameResult(
    val boardSize: Int,
    val pieceType: GameResultPieceType,
    val timeSpentMillis: Long,
    val piecePositions: Set<Pair<Int, Int>>,
)

package com.alan.queensland.game.api

data class ActiveGameState(
    val boardSize: Int,
    val pieceType: PieceType = PieceType.KNIGHT,
    val piecePositions: Set<BoardPosition> = emptySet(),
    val timeSpentMillis: Long = 0L,
)

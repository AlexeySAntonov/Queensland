package com.alan.queensland.game.api

data class ActiveGameState(
    val boardSize: Int,
    val queenPositions: Set<BoardPosition> = emptySet(),
    val timeSpentMillis: Long = 0L,
)

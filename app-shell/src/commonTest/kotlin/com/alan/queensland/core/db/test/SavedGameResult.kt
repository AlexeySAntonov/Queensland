package com.alan.queensland.core.db.test

internal data class SavedGameResult(
    val boardSize: Int,
    val timeSpentMillis: Long,
    val queenPositions: Set<Pair<Int, Int>>,
)

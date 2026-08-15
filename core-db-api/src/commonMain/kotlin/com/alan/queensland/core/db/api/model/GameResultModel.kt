package com.alan.queensland.core.db.api.model

data class GameResultModel(
    val uuid: String,
    val timeSpentMillis: Long,
    val createdAtMillis: Long,
    val boardSize: Int,
    val queenPositions: Set<Pair<Int, Int>>,
)

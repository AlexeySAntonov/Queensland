package com.alan.queensland.leaderboard.impl.ui

data class GameResultUiState(
    val uuid: String,
    val rank: Int,
    val formattedTimeSpent: String,
    val formattedCompletedAt: String,
    val queenPositions: Set<Pair<Int, Int>>,
)

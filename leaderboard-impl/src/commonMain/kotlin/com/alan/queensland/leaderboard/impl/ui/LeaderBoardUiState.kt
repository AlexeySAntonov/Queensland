package com.alan.queensland.leaderboard.impl.ui

data class LeaderBoardUiState(
    val results: List<GameResultUiState>,
)

data class GameResultUiState(
    val uuid: String,
    val boardSize: Int,
    val formattedTimeSpent: String,
    val completedAt: String,
)

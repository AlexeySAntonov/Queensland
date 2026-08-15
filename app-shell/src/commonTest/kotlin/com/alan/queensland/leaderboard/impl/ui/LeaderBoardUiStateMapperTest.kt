package com.alan.queensland.leaderboard.impl.ui

import com.alan.queensland.core.db.api.model.GameResultModel
import kotlin.test.Test
import kotlin.test.assertEquals

class LeaderBoardUiStateMapperTest {

    private val mapper = LeaderBoardUiStateMapper()

    @Test
    fun groupsByBoardSizeAndRanksFastestResultsFirst() {
        val state = mapper(
            listOf(
                gameResult(uuid = "slow-4", boardSize = 4, timeSpentMillis = 5_000),
                gameResult(uuid = "fast-5", boardSize = 5, timeSpentMillis = 1_000),
                gameResult(uuid = "fast-4", boardSize = 4, timeSpentMillis = 2_000),
            ),
        )

        assertEquals(listOf(4, 5), state.groups.map(LeaderBoardGroupUiState::boardSize))
        assertEquals(listOf("fast-4", "slow-4"), state.groups.first().results.map(GameResultUiState::uuid))
        assertEquals(listOf(1, 2), state.groups.first().results.map(GameResultUiState::rank))
    }

    @Test
    fun ranksEqualTimesByMostRecentCompletion() {
        val state = mapper(
            listOf(
                gameResult(uuid = "older", createdAtMillis = 1_000),
                gameResult(uuid = "newer", createdAtMillis = 2_000),
            ),
        )

        assertEquals(listOf("newer", "older"), state.groups.single().results.map(GameResultUiState::uuid))
    }

    private fun gameResult(
        uuid: String,
        boardSize: Int = 4,
        timeSpentMillis: Long = 1_000,
        createdAtMillis: Long = 0,
    ) = GameResultModel(
        uuid = uuid,
        boardSize = boardSize,
        timeSpentMillis = timeSpentMillis,
        createdAtMillis = createdAtMillis,
    )
}

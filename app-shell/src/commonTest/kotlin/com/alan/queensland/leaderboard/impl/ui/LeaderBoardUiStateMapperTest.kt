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

        val fourByFourGroup = state.groups.first { group -> group.boardSize == 4 }

        assertEquals(listOf(5, 4), state.groups.map(LeaderBoardGroupUiState::boardSize))
        assertEquals(listOf("fast-4", "slow-4"), fourByFourGroup.results.map(GameResultUiState::uuid))
        assertEquals(listOf(1, 2), fourByFourGroup.results.map(GameResultUiState::rank))
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

    @Test
    fun mapsQueenPositions() {
        val queenPositions = setOf(
            0 to 1,
            1 to 3,
        )

        val result = mapper(
            listOf(gameResult(uuid = "result", queenPositions = queenPositions)),
        ).groups.single().results.single()

        assertEquals(
            setOf(
                0 to 1,
                1 to 3,
            ),
            result.queenPositions,
        )
    }

    @Test
    fun formatsTimeSpentWithMilliseconds() {
        val result = mapper(
            listOf(
                gameResult(
                    uuid = "result",
                    timeSpentMillis = 3_661_234L,
                ),
            ),
        ).groups.single().results.single()

        assertEquals("1:01:01.234", result.formattedTimeSpent)
    }

    private fun gameResult(
        uuid: String,
        boardSize: Int = 4,
        timeSpentMillis: Long = 1_000,
        createdAtMillis: Long = 0,
        queenPositions: Set<Pair<Int, Int>> = emptySet(),
    ) = GameResultModel(
        uuid = uuid,
        boardSize = boardSize,
        timeSpentMillis = timeSpentMillis,
        createdAtMillis = createdAtMillis,
        queenPositions = queenPositions,
    )
}

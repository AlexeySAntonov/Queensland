package com.alan.queensland.game.impl.ui

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.impl.business.QueenPlacementValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameUiStateMapperTest {

    private val mapper = GameUiStateMapper()

    @Test
    fun mapsBoardStateAndCurrentSessionTime() {
        val queenPositions = setOf(
            BoardPosition(row = 0, column = 0),
            BoardPosition(row = 1, column = 2),
        )
        val conflictingPositions = setOf(BoardPosition(row = 0, column = 0))

        val result = mapper(
            state = ActiveGameState(
                boardSize = 4,
                queenPositions = queenPositions,
                timeSpentMillis = 1_250L,
            ),
            validation = QueenPlacementValidationResult(
                conflictingPositions = conflictingPositions,
                isSolved = false,
            ),
            currentSessionElapsedMillis = 750L,
        )

        assertEquals(4, result.boardSize)
        assertEquals(queenPositions, result.queenPositions)
        assertEquals(conflictingPositions, result.conflictingPositions)
        assertEquals(2, result.remainingQueenCount)
        assertEquals("00:02", result.formattedTimeSpent)
        assertFalse(result.isSolved)
    }

    @Test
    fun formatsElapsedTimeIncludingHours() {
        val result = mapper(
            state = ActiveGameState(
                boardSize = 4,
                timeSpentMillis = 3_600_000L,
            ),
            validation = QueenPlacementValidationResult(
                conflictingPositions = emptySet(),
                isSolved = true,
            ),
            currentSessionElapsedMillis = 61_000L,
        )

        assertEquals("1:01:01", result.formattedTimeSpent)
        assertTrue(result.isSolved)
    }
}

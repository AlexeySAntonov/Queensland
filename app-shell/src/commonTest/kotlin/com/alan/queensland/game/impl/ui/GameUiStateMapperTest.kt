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
        val firstQueen = BoardPosition(row = 0, column = 0)
        val secondQueen = BoardPosition(row = 1, column = 2)
        val queenPositions = setOf(
            firstQueen,
            secondQueen,
        )
        val conflictingPositions = setOf(firstQueen, secondQueen)
        val conflictingPairs = setOf(firstQueen to secondQueen)

        val result = mapper(
            state = ActiveGameState(
                boardSize = 4,
                queenPositions = queenPositions,
                timeSpentMillis = 1_250L,
            ),
            validation = QueenPlacementValidationResult(
                conflictingPositions = conflictingPositions,
                conflictingPairs = conflictingPairs,
                isSolved = false,
            ),
            currentSessionElapsedMillis = 750L,
        )

        assertEquals(4, result.boardSize)
        assertEquals(queenPositions, result.queenPositions)
        assertEquals(conflictingPositions, result.conflictingPositions)
        assertEquals(conflictingPairs, result.conflictingPairs)
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
                conflictingPairs = emptySet(),
                isSolved = true,
            ),
            currentSessionElapsedMillis = 61_000L,
        )

        assertEquals("1:01:01", result.formattedTimeSpent)
        assertTrue(result.isSolved)
    }
}

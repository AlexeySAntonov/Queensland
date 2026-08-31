package com.alan.queensland.game.impl.ui

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.api.PieceType
import com.alan.queensland.game.impl.business.PiecePlacementValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameUiStateMapperTest {

    private val mapper = GameUiStateMapper()

    @Test
    fun mapsBoardStateAndCurrentSessionTime() {
        val firstPiece = BoardPosition(row = 0, column = 0)
        val secondPiece = BoardPosition(row = 1, column = 2)
        val piecePositions = setOf(
            firstPiece,
            secondPiece,
        )
        val conflictingPositions = setOf(firstPiece, secondPiece)
        val conflictingPairs = setOf(firstPiece to secondPiece)

        val result = mapper(
            state = ActiveGameState(
                boardSize = 4,
                pieceType = PieceType.BISHOP,
                piecePositions = piecePositions,
                timeSpentMillis = 1_250L,
            ),
            validation = PiecePlacementValidationResult(
                conflictingPositions = conflictingPositions,
                conflictingPairs = conflictingPairs,
                isSolved = false,
            ),
            currentSessionElapsedMillis = 750L,
        )

        assertEquals(4, result.boardSize)
        assertEquals(PieceType.BISHOP, result.pieceType)
        assertEquals(piecePositions, result.piecePositions)
        assertEquals(conflictingPositions, result.conflictingPositions)
        assertEquals(conflictingPairs, result.conflictingPairs)
        assertEquals(2, result.remainingPieceCount)
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
            validation = PiecePlacementValidationResult(
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

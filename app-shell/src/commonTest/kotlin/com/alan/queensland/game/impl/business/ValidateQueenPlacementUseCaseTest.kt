package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateQueenPlacementUseCaseTest {

    private val validateQueenPlacementUseCase = ValidateQueenPlacementUseCase()

    @Test
    fun validPartialPlacementHasNoConflicts() {
        val state = ActiveGameState(
            boardSize = 4,
            queenPositions = setOf(
                BoardPosition(row = 0, column = 1),
                BoardPosition(row = 1, column = 3),
                BoardPosition(row = 2, column = 0),
            ),
        )

        val result = validateQueenPlacementUseCase(state)

        assertTrue(result.conflictingPositions.isEmpty())
        assertTrue(result.conflictingPairs.isEmpty())
        assertFalse(result.isSolved)
    }

    @Test
    fun returnsEveryConflictingPair() {
        val firstQueen = BoardPosition(row = 0, column = 0)
        val rowConflict = BoardPosition(row = 0, column = 3)
        val diagonalConflict = BoardPosition(row = 2, column = 2)

        val result = validateQueenPlacementUseCase(
            ActiveGameState(
                boardSize = 4,
                queenPositions = setOf(firstQueen, rowConflict, diagonalConflict),
            ),
        )

        assertEquals(
            setOf(
                firstQueen to rowConflict,
                firstQueen to diagonalConflict,
            ),
            result.conflictingPairs,
        )
    }

    @Test
    fun rowAndColumnConflictsMarkEveryInvolvedQueen() {
        val positions = setOf(
            BoardPosition(row = 0, column = 0),
            BoardPosition(row = 0, column = 2),
            BoardPosition(row = 1, column = 3),
            BoardPosition(row = 3, column = 3),
        )

        val result = validateQueenPlacementUseCase(
            ActiveGameState(boardSize = 4, queenPositions = positions),
        )

        assertEquals(positions, result.conflictingPositions)
        assertFalse(result.isSolved)
    }

    @Test
    fun bothDiagonalDirectionsMarkEveryInvolvedQueen() {
        val positions = setOf(
            BoardPosition(row = 0, column = 0),
            BoardPosition(row = 2, column = 2),
            BoardPosition(row = 1, column = 7),
            BoardPosition(row = 5, column = 3),
        )

        val result = validateQueenPlacementUseCase(
            ActiveGameState(boardSize = 8, queenPositions = positions),
        )

        assertEquals(positions, result.conflictingPositions)
        assertFalse(result.isSolved)
    }

    @Test
    fun completeValidPlacementIsSolved() {
        val state = ActiveGameState(
            boardSize = 4,
            queenPositions = setOf(
                BoardPosition(row = 0, column = 1),
                BoardPosition(row = 1, column = 3),
                BoardPosition(row = 2, column = 0),
                BoardPosition(row = 3, column = 2),
            ),
        )

        val result = validateQueenPlacementUseCase(state)

        assertTrue(result.conflictingPositions.isEmpty())
        assertTrue(result.conflictingPairs.isEmpty())
        assertTrue(result.isSolved)
    }
}

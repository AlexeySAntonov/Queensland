package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.api.PieceType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidatePiecePlacementUseCaseTest {

    private val validatePiecePlacementUseCase = ValidatePiecePlacementUseCase()

    @Test
    fun knightValidationFindsBothAttackOffsets() {
        val firstPiece = BoardPosition(row = 0, column = 0)
        val firstConflict = BoardPosition(row = 1, column = 2)
        val secondConflict = BoardPosition(row = 2, column = 1)
        val safePosition = BoardPosition(row = 0, column = 3)

        val result = validatePiecePlacementUseCase(
            ActiveGameState(
                boardSize = 4,
                pieceType = PieceType.KNIGHT,
                piecePositions = setOf(
                    firstPiece,
                    firstConflict,
                    secondConflict,
                    safePosition,
                ),
            ),
        )

        assertEquals(
            setOf(
                firstPiece to firstConflict,
                firstPiece to secondConflict,
            ),
            result.conflictingPairs,
        )
        assertEquals(
            setOf(firstPiece, firstConflict, secondConflict),
            result.conflictingPositions,
        )
    }

    @Test
    fun bishopValidationOnlyFindsDiagonalConflicts() {
        val firstPiece = BoardPosition(row = 0, column = 0)
        val diagonalConflict = BoardPosition(row = 2, column = 2)
        val sameRow = BoardPosition(row = 0, column = 3)

        val result = validatePiecePlacementUseCase(
            ActiveGameState(
                boardSize = 4,
                pieceType = PieceType.BISHOP,
                piecePositions = setOf(firstPiece, diagonalConflict, sameRow),
            ),
        )

        assertEquals(setOf(firstPiece to diagonalConflict), result.conflictingPairs)
        assertEquals(setOf(firstPiece, diagonalConflict), result.conflictingPositions)
    }

    @Test
    fun rookValidationOnlyFindsRowAndColumnConflicts() {
        val firstPiece = BoardPosition(row = 0, column = 0)
        val rowConflict = BoardPosition(row = 0, column = 3)
        val columnConflict = BoardPosition(row = 2, column = 0)
        val diagonalPosition = BoardPosition(row = 1, column = 1)

        val result = validatePiecePlacementUseCase(
            ActiveGameState(
                boardSize = 4,
                pieceType = PieceType.ROOK,
                piecePositions = setOf(
                    firstPiece,
                    rowConflict,
                    columnConflict,
                    diagonalPosition,
                ),
            ),
        )

        assertEquals(
            setOf(
                firstPiece to rowConflict,
                firstPiece to columnConflict,
            ),
            result.conflictingPairs,
        )
        assertEquals(
            setOf(firstPiece, rowConflict, columnConflict),
            result.conflictingPositions,
        )
    }

    @Test
    fun completeConflictFreePlacementIsSolved() {
        val state = ActiveGameState(
            boardSize = 4,
            pieceType = PieceType.ROOK,
            piecePositions = setOf(
                BoardPosition(row = 0, column = 0),
                BoardPosition(row = 1, column = 1),
                BoardPosition(row = 2, column = 2),
                BoardPosition(row = 3, column = 3),
            ),
        )

        val result = validatePiecePlacementUseCase(state)

        assertTrue(result.conflictingPositions.isEmpty())
        assertTrue(result.conflictingPairs.isEmpty())
        assertTrue(result.isSolved)
    }

    @Test
    fun conflictFreePartialPlacementIsNotSolved() {
        val result = validatePiecePlacementUseCase(
            ActiveGameState(
                boardSize = 4,
                pieceType = PieceType.KNIGHT,
                piecePositions = setOf(
                    BoardPosition(row = 0, column = 0),
                    BoardPosition(row = 0, column = 1),
                    BoardPosition(row = 0, column = 2),
                ),
            ),
        )

        assertTrue(result.conflictingPositions.isEmpty())
        assertFalse(result.isSolved)
    }
}

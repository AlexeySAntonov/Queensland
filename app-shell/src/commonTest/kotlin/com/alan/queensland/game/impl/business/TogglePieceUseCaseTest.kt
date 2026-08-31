package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.test.FakeGameRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class TogglePieceUseCaseTest {

    @Test
    fun tappingEmptyAndOccupiedCellAddsAndRemovesPiece() {
        val repository = FakeGameRepository(ActiveGameState(boardSize = 4))
        val togglePiece = TogglePieceUseCase(repository)
        val position = BoardPosition(row = 1, column = 2)

        togglePiece(position)
        assertEquals(setOf(position), repository.currentState?.piecePositions)

        togglePiece(position)
        assertEquals(emptySet(), repository.currentState?.piecePositions)
    }

    @Test
    fun placementDoesNotExceedBoardSize() {
        val initialPositions = setOf(
            BoardPosition(row = 0, column = 0),
            BoardPosition(row = 0, column = 1),
            BoardPosition(row = 0, column = 2),
            BoardPosition(row = 0, column = 3),
        )
        val repository = FakeGameRepository(
            ActiveGameState(boardSize = 4, piecePositions = initialPositions),
        )

        TogglePieceUseCase(repository)(BoardPosition(row = 1, column = 0))

        assertEquals(initialPositions, repository.currentState?.piecePositions)
    }

    @Test
    fun positionOutsideBoardIsIgnored() {
        val initialState = ActiveGameState(boardSize = 4)
        val repository = FakeGameRepository(initialState)

        TogglePieceUseCase(repository)(BoardPosition(row = 4, column = 0))

        assertEquals(initialState, repository.currentState)
    }
}

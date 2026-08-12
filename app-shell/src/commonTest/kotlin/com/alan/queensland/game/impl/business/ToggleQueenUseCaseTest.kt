package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.test.FakeGameRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ToggleQueenUseCaseTest {

    @Test
    fun tappingEmptyAndOccupiedCellAddsAndRemovesQueen() {
        val repository = FakeGameRepository(ActiveGameState(boardSize = 4))
        val toggleQueen = ToggleQueenUseCase(repository)
        val position = BoardPosition(row = 1, column = 2)

        toggleQueen(position)
        assertEquals(setOf(position), repository.currentState?.queenPositions)

        toggleQueen(position)
        assertEquals(emptySet(), repository.currentState?.queenPositions)
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
            ActiveGameState(boardSize = 4, queenPositions = initialPositions),
        )

        ToggleQueenUseCase(repository)(BoardPosition(row = 1, column = 0))

        assertEquals(initialPositions, repository.currentState?.queenPositions)
    }

    @Test
    fun positionOutsideBoardIsIgnored() {
        val initialState = ActiveGameState(boardSize = 4)
        val repository = FakeGameRepository(initialState)

        ToggleQueenUseCase(repository)(BoardPosition(row = 4, column = 0))

        assertEquals(initialState, repository.currentState)
    }
}

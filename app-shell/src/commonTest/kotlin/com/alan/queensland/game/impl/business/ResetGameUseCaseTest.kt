package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.test.FakeGameRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ResetGameUseCaseTest {

    @Test
    fun clearsQueensAndElapsedTimeWhileKeepingBoardSize() {
        val repository = FakeGameRepository(
            ActiveGameState(
                boardSize = 6,
                queenPositions = setOf(BoardPosition(row = 1, column = 2)),
                timeSpentMillis = 12_500L,
            ),
        )

        ResetGameUseCase(repository)()

        assertEquals(
            ActiveGameState(boardSize = 6),
            repository.currentState,
        )
    }
}

package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.api.PieceType
import com.alan.queensland.game.test.FakeGameRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ResetGameUseCaseTest {

    @Test
    fun clearsPiecesAndElapsedTimeWhileKeepingConfiguration() {
        val repository = FakeGameRepository(
            ActiveGameState(
                boardSize = 6,
                pieceType = PieceType.ROOK,
                piecePositions = setOf(BoardPosition(row = 1, column = 2)),
                timeSpentMillis = 12_500L,
            ),
        )

        ResetGameUseCase(repository)()

        assertEquals(
            ActiveGameState(
                boardSize = 6,
                pieceType = PieceType.ROOK,
            ),
            repository.currentState,
        )
    }
}

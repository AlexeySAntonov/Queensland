package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.test.FakeGameRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class AddElapsedGameTimeUseCaseTest {

    @Test
    fun elapsedTimeIsAddedToActiveGame() {
        val initialState = ActiveGameState(boardSize = 4, timeSpentMillis = 1_250L)
        val repository = FakeGameRepository(initialState)

        AddElapsedGameTimeUseCase(repository)(elapsedMillis = 750L)

        assertEquals(2_000L, repository.currentState?.timeSpentMillis)
    }

    @Test
    fun nonPositiveElapsedTimeIsIgnored() {
        val initialState = ActiveGameState(boardSize = 4, timeSpentMillis = 1_250L)
        val repository = FakeGameRepository(initialState)
        val addElapsedGameTime = AddElapsedGameTimeUseCase(repository)

        addElapsedGameTime(elapsedMillis = 0L)
        addElapsedGameTime(elapsedMillis = -500L)

        assertEquals(initialState, repository.currentState)
    }
}

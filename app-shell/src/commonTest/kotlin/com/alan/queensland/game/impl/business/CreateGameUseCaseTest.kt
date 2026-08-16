package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.api.GameBoardSize
import com.alan.queensland.game.api.StartNewGameUseCase
import com.alan.queensland.game.test.FakeGameRepository
import com.alan.queensland.navigation.api.NavigationEvent
import com.alan.queensland.navigation.test.FakeRouter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CreateGameUseCaseTest {

    @Test
    fun openingGameConfigurationPreservesActiveGame() {
        val activeGame = ActiveGameState(
            boardSize = 4,
            queenPositions = setOf(BoardPosition(row = 1, column = 2)),
            timeSpentMillis = 1_000L,
        )
        val repository = FakeGameRepository(initialState = activeGame)
        val router = FakeRouter()

        StartNewGameUseCase(router)()

        assertEquals(activeGame, repository.currentState)
        assertEquals(0, repository.clearInvocationCount)
        assertEquals(
            listOf<NavigationEvent>(NavigationEvent.OpenGameConfiguration),
            router.sentEvents,
        )
    }

    @Test
    fun creatingGameClearsPreviousStateAndOpensGame() {
        val repository = FakeGameRepository(
            initialState = ActiveGameState(
                boardSize = 4,
                queenPositions = setOf(BoardPosition(row = 1, column = 2)),
                timeSpentMillis = 1_000L,
            ),
        )
        val router = FakeRouter()

        val result = CreateGameUseCase(repository, router)(boardSize = 8)

        assertTrue(result.isSuccess)
        assertEquals(1, repository.clearInvocationCount)
        assertEquals(ActiveGameState(boardSize = 8), repository.currentState)
        assertEquals(listOf<NavigationEvent>(NavigationEvent.OpenGame), router.sentEvents)
    }

    @Test
    fun creatingGameRejectsUnsupportedBoardSizeWithoutChangingActiveGame() {
        listOf(GameBoardSize.MIN - 1, GameBoardSize.MAX + 1).forEach { boardSize ->
            val activeGame = ActiveGameState(boardSize = GameBoardSize.MIN)
            val repository = FakeGameRepository(initialState = activeGame)
            val router = FakeRouter()

            val result = CreateGameUseCase(repository, router)(boardSize)

            assertIs<IllegalArgumentException>(result.exceptionOrNull())
            assertEquals(activeGame, repository.currentState)
            assertEquals(0, repository.clearInvocationCount)
            assertEquals(listOf<NavigationEvent>(NavigationEvent.Back), router.sentEvents)
        }
    }
}

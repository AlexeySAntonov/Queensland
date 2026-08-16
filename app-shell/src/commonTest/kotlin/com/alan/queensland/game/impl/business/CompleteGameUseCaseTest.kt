package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.test.FakeGameRepository
import com.alan.queensland.navigation.api.NavigationEvent
import com.alan.queensland.navigation.test.FakeRouter
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CompleteGameUseCaseTest {

    @Test
    fun savesResultClearsGameAndOpensFinishedScreen() = runTest {
        val gameState = ActiveGameState(
            boardSize = 6,
            timeSpentMillis = 12_345L,
        )
        val repository = FakeGameRepository(gameState)
        val router = FakeRouter()

        val result = CompleteGameUseCase(
            gameRepository = repository,
            router = router,
        )()

        assertTrue(result.isSuccess)
        assertEquals(listOf(gameState), repository.completedGames)
        assertNull(repository.currentState)
        assertEquals(listOf<NavigationEvent>(NavigationEvent.OpenGameFinished), router.sentEvents)
    }

    @Test
    fun failsWhenThereIsNoActiveGame() = runTest {
        val repository = FakeGameRepository()
        val router = FakeRouter()

        val result = CompleteGameUseCase(
            gameRepository = repository,
            router = router,
        )()

        assertIs<IllegalStateException>(result.exceptionOrNull())
        assertEquals(emptyList<ActiveGameState>(), repository.completedGames)
        assertEquals(emptyList<NavigationEvent>(), router.sentEvents)
    }
}

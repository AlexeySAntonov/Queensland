package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.test.FakeGameRepository
import com.alan.queensland.navigation.api.NavigationEvent
import com.alan.queensland.navigation.test.FakeRouter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AbandonGameUseCaseTest {

    @Test
    fun clearsActiveGameAndNavigatesBack() {
        val repository = FakeGameRepository(ActiveGameState(boardSize = 6))
        val router = FakeRouter()

        AbandonGameUseCase(repository, router)()

        assertNull(repository.currentState)
        assertEquals(1, repository.clearInvocationCount)
        assertEquals(listOf<NavigationEvent>(NavigationEvent.Back), router.sentEvents)
    }
}

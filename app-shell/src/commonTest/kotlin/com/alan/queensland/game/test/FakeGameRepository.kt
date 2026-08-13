package com.alan.queensland.game.test

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeGameRepository(
    initialState: ActiveGameState? = null,
) : GameRepository {

    private val state = MutableStateFlow(initialState)

    val currentState: ActiveGameState?
        get() = state.value

    val completedGames = mutableListOf<ActiveGameState>()

    override fun observeActiveGameState(): Flow<ActiveGameState?> = state

    override fun updateActiveGameState(
        transform: ActiveGameState?.() -> ActiveGameState?,
    ) {
        state.value = state.value.transform()
    }

    override suspend fun completeActiveGame(): Boolean {
        val completedGame = state.value ?: return false
        completedGames += completedGame
        state.value = null
        return true
    }

    override fun clear() {
        state.value = null
    }
}

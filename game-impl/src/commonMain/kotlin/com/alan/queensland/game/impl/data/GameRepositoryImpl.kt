package com.alan.queensland.game.impl.data

import com.alan.queensland.core.di.Singleton
import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class GameRepositoryImpl : GameRepository {

    private val activeGameState = MutableStateFlow<ActiveGameState?>(null)

    override fun observeActiveGameState() = activeGameState.asStateFlow()

    override fun updateActiveGameState(transform: ActiveGameState?.() -> ActiveGameState?) {
        activeGameState.update { currentState -> currentState.transform() }
    }

    override fun clear() {
        activeGameState.value = null
    }
}

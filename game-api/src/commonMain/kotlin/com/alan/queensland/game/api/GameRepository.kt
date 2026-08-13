package com.alan.queensland.game.api

import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun observeActiveGameState(): Flow<ActiveGameState?>

    fun updateActiveGameState(transform: ActiveGameState?.() -> ActiveGameState?)

    suspend fun completeActiveGame(): Boolean

    fun clear()
}

package com.alan.queensland.game.api

import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun observeHasActiveGame(): Flow<Boolean>

    fun clearCache()
}

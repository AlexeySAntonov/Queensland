package com.alan.queensland.game.impl.data

import com.alan.queensland.core.di.Singleton
import com.alan.queensland.game.api.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class GameRepositoryImpl : GameRepository {

    private val _hasActiveGame = MutableStateFlow(false)

    override fun observeHasActiveGame() = _hasActiveGame

    override fun clearCache() {
        _hasActiveGame.value = false
    }
}

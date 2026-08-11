package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.GameRepository
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveActiveGameStateUseCase(
    private val repository: GameRepository,
) {
    operator fun invoke() = repository.observeActiveGameState()
}

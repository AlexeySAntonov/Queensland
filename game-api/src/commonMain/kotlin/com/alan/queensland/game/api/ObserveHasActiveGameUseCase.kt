package com.alan.queensland.game.api

import me.tatarka.inject.annotations.Inject

@Inject
class ObserveHasActiveGameUseCase(
    private val repository: GameRepository,
) {
    operator fun invoke() = repository.observeHasActiveGame()
}
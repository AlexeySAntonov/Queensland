package com.alan.queensland.game.api

import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Inject

@Inject
class CreateGameUseCase(
    private val gameRepository: GameRepository,
    private val router: Router,
) {
    operator fun invoke(boardSize: Int) {
        gameRepository.updateActiveGameState { ActiveGameState(boardSize = boardSize) }
        router.openGame()
    }
}

package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Inject

@Inject
class CreateGameUseCase(
    private val gameRepository: GameRepository,
    private val router: Router,
) {
    operator fun invoke(boardSize: Int) {
        gameRepository.clear()
        gameRepository.updateActiveGameState { ActiveGameState(boardSize = boardSize) }
        router.openGame()
    }
}

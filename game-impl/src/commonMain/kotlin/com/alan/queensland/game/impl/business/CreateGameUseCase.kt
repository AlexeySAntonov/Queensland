package com.alan.queensland.game.impl.business

import co.touchlab.kermit.Logger
import com.alan.queensland.core.utils.result.resultOf
import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.GameBoardSize
import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Inject

@Inject
class CreateGameUseCase(
    private val gameRepository: GameRepository,
    private val router: Router,
) {

    private val logger = Logger.withTag("CreateGameUseCase")

    operator fun invoke(boardSize: Int): Result<Unit> =
        resultOf {
            GameBoardSize.requireSupported(boardSize)
            gameRepository.clear()
            gameRepository.updateActiveGameState { ActiveGameState(boardSize = boardSize) }
            router.openGame()
        }.onFailure { throwable ->
            logger.e(throwable) { "Failed to create game" }
            router.back()
        }
}

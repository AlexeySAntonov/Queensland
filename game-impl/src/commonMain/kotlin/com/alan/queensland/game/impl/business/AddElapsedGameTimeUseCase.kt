package com.alan.queensland.game.impl.business

import co.touchlab.kermit.Logger
import com.alan.queensland.game.api.GameRepository
import me.tatarka.inject.annotations.Inject

@Inject
class AddElapsedGameTimeUseCase(
    private val gameRepository: GameRepository,
) {
    private val logger = Logger.withTag("AddElapsedGameTimeUseCase")

    operator fun invoke(elapsedMillis: Long) {
        if (elapsedMillis <= 0L) {
            logger.e { "Elapsed game time must be positive, received $elapsedMillis ms" }
            return
        }

        gameRepository.updateActiveGameState {
            this?.copy(timeSpentMillis = timeSpentMillis + elapsedMillis)
        }
    }
}
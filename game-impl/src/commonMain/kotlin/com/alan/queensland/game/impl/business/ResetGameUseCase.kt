package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.GameRepository
import me.tatarka.inject.annotations.Inject

@Inject
class ResetGameUseCase(
    private val gameRepository: GameRepository,
) {
    operator fun invoke() {
        gameRepository.updateActiveGameState {
            this?.copy(
                queenPositions = emptySet(),
                timeSpentMillis = 0L,
            )
        }
    }
}

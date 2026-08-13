package com.alan.queensland.game.impl.business

import co.touchlab.kermit.Logger
import com.alan.queensland.core.utils.result.resultOf
import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Inject

@Inject
class CompleteGameUseCase(
    private val gameRepository: GameRepository,
    private val router: Router,
) {

    private val logger = Logger.withTag("CompleteGameUseCase")

    suspend operator fun invoke(): Result<Unit> =
        resultOf {
            if (gameRepository.completeActiveGame()) {
                router.back()
            }
        }.onFailure { throwable ->
            logger.e(throwable) { "Failed to complete game" }
        }
}

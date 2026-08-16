package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Inject

@Inject
class AbandonGameUseCase(
    private val gameRepository: GameRepository,
    private val router: Router,
) {

    operator fun invoke() {
        gameRepository.clear()
        router.back()
    }
}

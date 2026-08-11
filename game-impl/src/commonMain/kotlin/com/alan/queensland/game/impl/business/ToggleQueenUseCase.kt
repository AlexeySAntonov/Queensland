package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.api.GameRepository
import me.tatarka.inject.annotations.Inject

@Inject
class ToggleQueenUseCase(
    private val gameRepository: GameRepository,
) {
    operator fun invoke(position: BoardPosition) {
        gameRepository.updateActiveGameState {
            this?.run {
                if (position.row !in 0 until boardSize || position.column !in 0 until boardSize) {
                    return@run this
                }

                when {
                    position in queenPositions -> copy(queenPositions = queenPositions - position)
                    queenPositions.size < boardSize -> copy(queenPositions = queenPositions + position)
                    else -> this
                }
            }
        }
    }
}

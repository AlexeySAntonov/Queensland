package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.api.GameRepository
import me.tatarka.inject.annotations.Inject

@Inject
class TogglePieceUseCase(
    private val gameRepository: GameRepository,
) {
    operator fun invoke(position: BoardPosition) {
        gameRepository.updateActiveGameState {
            this?.run {
                if (position.row !in 0 until boardSize || position.column !in 0 until boardSize) {
                    return@run this
                }

                when {
                    position in piecePositions -> copy(piecePositions = piecePositions - position)
                    piecePositions.size < boardSize -> copy(piecePositions = piecePositions + position)
                    else -> this
                }
            }
        }
    }
}

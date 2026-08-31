package com.alan.queensland.game.impl.data

import com.alan.queensland.core.db.api.GameResultsDatasource
import com.alan.queensland.core.db.api.model.GameResultPieceType
import com.alan.queensland.core.di.Singleton
import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.game.api.PieceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class GameRepositoryImpl(
    private val gameResultsDatasource: GameResultsDatasource,
) : GameRepository {

    private val activeGameState = MutableStateFlow<ActiveGameState?>(null)

    override fun observeActiveGameState() = activeGameState.asStateFlow()

    override fun updateActiveGameState(transform: ActiveGameState?.() -> ActiveGameState?) {
        activeGameState.update { currentState -> currentState.transform() }
    }

    override suspend fun completeActiveGame(): Boolean {
        val completedGame = activeGameState.value ?: return false

        gameResultsDatasource.saveResult(
            boardSize = completedGame.boardSize,
            pieceType = completedGame.pieceType.toResultPieceType(),
            timeSpentMillis = completedGame.timeSpentMillis,
            piecePositions = completedGame.piecePositions.mapTo(mutableSetOf()) { position ->
                position.row to position.column
            },
        )

        clear()
        return true
    }

    override fun clear() {
        activeGameState.value = null
    }

    private fun PieceType.toResultPieceType() = when (this) {
        PieceType.KNIGHT -> GameResultPieceType.KNIGHT
        PieceType.BISHOP -> GameResultPieceType.BISHOP
        PieceType.ROOK -> GameResultPieceType.ROOK
    }
}

package com.alan.queensland.game.impl.ui

import com.alan.queensland.core.utils.time.formatElapsedTime
import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.impl.business.PiecePlacementValidationResult
import me.tatarka.inject.annotations.Inject

@Inject
class GameUiStateMapper {

    operator fun invoke(
        state: ActiveGameState,
        validation: PiecePlacementValidationResult,
        currentSessionElapsedMillis: Long,
    ) = GameUiState(
        boardSize = state.boardSize,
        pieceType = state.pieceType,
        piecePositions = state.piecePositions,
        conflictingPositions = validation.conflictingPositions,
        conflictingPairs = validation.conflictingPairs,
        remainingPieceCount = state.boardSize - state.piecePositions.size,
        formattedTimeSpent = formatElapsedTime(
            state.timeSpentMillis + currentSessionElapsedMillis,
        ),
        isSolved = validation.isSolved,
    )

}

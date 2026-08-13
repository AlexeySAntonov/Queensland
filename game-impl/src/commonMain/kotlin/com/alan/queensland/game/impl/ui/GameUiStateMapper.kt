package com.alan.queensland.game.impl.ui

import com.alan.queensland.core.utils.time.formatElapsedTime
import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.impl.business.QueenPlacementValidationResult
import me.tatarka.inject.annotations.Inject

@Inject
class GameUiStateMapper {

    operator fun invoke(
        state: ActiveGameState,
        validation: QueenPlacementValidationResult,
        currentSessionElapsedMillis: Long,
    ) = GameUiState(
        boardSize = state.boardSize,
        queenPositions = state.queenPositions,
        conflictingPositions = validation.conflictingPositions,
        remainingQueenCount = state.boardSize - state.queenPositions.size,
        formattedTimeSpent = formatElapsedTime(
            state.timeSpentMillis + currentSessionElapsedMillis,
        ),
        isSolved = validation.isSolved,
    )

}

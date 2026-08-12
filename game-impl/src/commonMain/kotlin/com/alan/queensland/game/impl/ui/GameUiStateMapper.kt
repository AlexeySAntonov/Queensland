package com.alan.queensland.game.impl.ui

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
    )

    private fun formatElapsedTime(timeSpentMillis: Long): String {
        val totalSeconds = timeSpentMillis.coerceAtLeast(0L) / MILLIS_PER_SECOND
        val hours = totalSeconds / SECONDS_PER_HOUR
        val minutes = (totalSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
        val seconds = totalSeconds % SECONDS_PER_MINUTE

        return if (hours > 0L) {
            "$hours:${minutes.toTwoDigits()}:${seconds.toTwoDigits()}"
        } else {
            "${minutes.toTwoDigits()}:${seconds.toTwoDigits()}"
        }
    }

    private fun Long.toTwoDigits() = toString().padStart(length = 2, padChar = '0')

    private companion object {
        const val MILLIS_PER_SECOND = 1_000L
        const val SECONDS_PER_MINUTE = 60L
        const val SECONDS_PER_HOUR = 3_600L
    }
}

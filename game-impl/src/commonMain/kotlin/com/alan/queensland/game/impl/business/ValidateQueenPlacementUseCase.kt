package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import me.tatarka.inject.annotations.Inject

@Inject
class ValidateQueenPlacementUseCase {

    /**
     * Validates every queen in [state] against row, column, and both diagonal constraints.
     *
     * The result contains every queen that shares at least one attack line with another queen.
     * The placement is solved only when the board contains exactly [ActiveGameState.boardSize]
     * queens and none of them conflict. Positions are expected to be within the board bounds.
     *
     * Runs in O(n + q) time and uses O(n) additional space, where `n` is the board size and `q`
     * is the number of placed queens.
     */
    operator fun invoke(state: ActiveGameState): QueenPlacementValidationResult {
        val attackLineCounts = QueenAttackLineCounts(state.boardSize)
        state.queenPositions.forEach(attackLineCounts::record)

        val conflictingPositions = state.queenPositions.filterTo(
            destination = mutableSetOf(),
            predicate = attackLineCounts::hasConflict,
        )

        return QueenPlacementValidationResult(
            conflictingPositions = conflictingPositions,
            isSolved = state.queenPositions.size == state.boardSize && conflictingPositions.isEmpty(),
        )
    }

    private class QueenAttackLineCounts(
        private val boardSize: Int,
    ) {
        private val rowCounts = IntArray(boardSize)
        private val columnCounts = IntArray(boardSize)
        private val descendingDiagonalCounts = IntArray(2 * boardSize - 1)
        private val ascendingDiagonalCounts = IntArray(2 * boardSize - 1)

        fun record(position: BoardPosition) {
            rowCounts[position.row]++
            columnCounts[position.column]++
            descendingDiagonalCounts[position.descendingDiagonalIndex()]++
            ascendingDiagonalCounts[position.ascendingDiagonalIndex()]++
        }

        fun hasConflict(position: BoardPosition): Boolean {
            return rowCounts[position.row] > 1 ||
                columnCounts[position.column] > 1 ||
                descendingDiagonalCounts[position.descendingDiagonalIndex()] > 1 ||
                ascendingDiagonalCounts[position.ascendingDiagonalIndex()] > 1
        }

        private fun BoardPosition.descendingDiagonalIndex() = row - column + boardSize - 1

        private fun BoardPosition.ascendingDiagonalIndex() = row + column
    }
}

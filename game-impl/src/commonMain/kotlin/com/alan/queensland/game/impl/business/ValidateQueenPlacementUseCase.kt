package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import me.tatarka.inject.annotations.Inject

@Inject
class ValidateQueenPlacementUseCase {

    /**
     * Validates every queen in [state] against row, column, and both diagonal constraints.
     *
     * The result contains every queen that shares at least one attack line with another queen and
     * every conflicting pair. The placement is solved only when the board contains exactly
     * [ActiveGameState.boardSize] queens and none of them conflict. Positions are expected to be
     * within the board bounds.
     *
     * Runs in O(n + q + c) time and uses O(n + q + c) additional space, where `n` is the board
     * size, `q` is the number of placed queens, and `c` is the number of conflicting pairs.
     */
    operator fun invoke(state: ActiveGameState): QueenPlacementValidationResult {
        val attackLines = QueenAttackLines(state.boardSize)
        state.queenPositions.forEach(attackLines::record)

        val conflictingPositions = state.queenPositions.filterTo(
            destination = mutableSetOf(),
            predicate = attackLines::hasConflict,
        )

        return QueenPlacementValidationResult(
            conflictingPositions = conflictingPositions,
            conflictingPairs = attackLines.conflictingPairs(),
            isSolved = state.queenPositions.size == state.boardSize && conflictingPositions.isEmpty(),
        )
    }

    private class QueenAttackLines(
        private val boardSize: Int,
    ) {
        private val rows = Array(boardSize) { mutableListOf<BoardPosition>() }
        private val columns = Array(boardSize) { mutableListOf<BoardPosition>() }
        private val descendingDiagonals =
            Array(2 * boardSize - 1) { mutableListOf<BoardPosition>() }
        private val ascendingDiagonals =
            Array(2 * boardSize - 1) { mutableListOf<BoardPosition>() }

        fun record(position: BoardPosition) {
            rows[position.row] += position
            columns[position.column] += position
            descendingDiagonals[position.descendingDiagonalIndex()] += position
            ascendingDiagonals[position.ascendingDiagonalIndex()] += position
        }

        fun hasConflict(position: BoardPosition): Boolean {
            return rows[position.row].size > 1 ||
                columns[position.column].size > 1 ||
                descendingDiagonals[position.descendingDiagonalIndex()].size > 1 ||
                ascendingDiagonals[position.ascendingDiagonalIndex()].size > 1
        }

        fun conflictingPairs(): Set<Pair<BoardPosition, BoardPosition>> = buildSet {
            rows.forEach { positions -> positions.addPairsTo(this) }
            columns.forEach { positions -> positions.addPairsTo(this) }
            descendingDiagonals.forEach { positions -> positions.addPairsTo(this) }
            ascendingDiagonals.forEach { positions -> positions.addPairsTo(this) }
        }

        private fun List<BoardPosition>.addPairsTo(
            destination: MutableSet<Pair<BoardPosition, BoardPosition>>,
        ) {
            for (firstIndex in 0 until lastIndex) {
                for (secondIndex in firstIndex + 1..lastIndex) {
                    destination += this[firstIndex] to this[secondIndex]
                }
            }
        }

        private fun BoardPosition.descendingDiagonalIndex() = row - column + boardSize - 1

        private fun BoardPosition.ascendingDiagonalIndex() = row + column
    }
}

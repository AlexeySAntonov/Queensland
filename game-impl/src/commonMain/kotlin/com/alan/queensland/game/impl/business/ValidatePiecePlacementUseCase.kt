package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.api.PieceType
import me.tatarka.inject.annotations.Inject
import kotlin.math.absoluteValue

@Inject
class ValidatePiecePlacementUseCase {

    /**
     * Validates every placed piece using the attack rules of [ActiveGameState.pieceType].
     *
     * Rooks conflict on a shared row or column, bishops on a shared diagonal, and knights when
     * their row and column distances are one and two squares. The result contains both every
     * conflicting pair and every position involved in a conflict. A placement is solved when the
     * board contains exactly [ActiveGameState.boardSize] pieces and no pair conflicts.
     *
     * Runs in O(p^2) time and O(c) additional space, where `p` is the number of placed pieces and
     * `c` is the number of conflicting pairs. Since placement is capped at the board size, the
     * current maximum board performs at most 66 pair checks.
     */
    operator fun invoke(state: ActiveGameState): PiecePlacementValidationResult {
        val positions = state.piecePositions.toList()
        val conflictingPairs = buildSet {
            for (firstIndex in 0 until positions.lastIndex) {
                for (secondIndex in firstIndex + 1..positions.lastIndex) {
                    val first = positions[firstIndex]
                    val second = positions[secondIndex]
                    if (state.pieceType.piecesConflict(first, second)) {
                        add(first to second)
                    }
                }
            }
        }
        val conflictingPositions = conflictingPairs.flatMapTo(mutableSetOf()) { pair ->
            listOf(pair.first, pair.second)
        }

        return PiecePlacementValidationResult(
            conflictingPositions = conflictingPositions,
            conflictingPairs = conflictingPairs,
            isSolved = state.piecePositions.size == state.boardSize && conflictingPositions.isEmpty(),
        )
    }

    private fun PieceType.piecesConflict(
        first: BoardPosition,
        second: BoardPosition,
    ): Boolean {
        val rowDistance = (first.row - second.row).absoluteValue
        val columnDistance = (first.column - second.column).absoluteValue

        return when (this) {
            PieceType.KNIGHT ->
                rowDistance == 1 && columnDistance == 2 ||
                    rowDistance == 2 && columnDistance == 1

            PieceType.BISHOP -> rowDistance == columnDistance
            PieceType.ROOK -> rowDistance == 0 || columnDistance == 0
        }
    }
}

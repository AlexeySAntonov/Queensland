package com.alan.queensland.game.impl.business

import com.alan.queensland.game.api.BoardPosition

data class QueenPlacementValidationResult(
    val conflictingPositions: Set<BoardPosition>,
    val isSolved: Boolean,
)

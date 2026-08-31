package com.alan.queensland.game.impl.ui.configuration

import com.alan.queensland.game.api.GameBoardSize
import com.alan.queensland.game.api.PieceType

data class GameConfigurationUiState(
    val boardSize: Int = GameBoardSize.DEFAULT,
    val selectedPieceType: PieceType = PieceType.KNIGHT,
)

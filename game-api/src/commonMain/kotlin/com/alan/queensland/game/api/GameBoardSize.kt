package com.alan.queensland.game.api

object GameBoardSize {
    const val MIN = 4
    const val DEFAULT = 8
    const val MAX = 12

    fun requireSupported(boardSize: Int) {
        require(boardSize in MIN..MAX) {
            "Board size must be between $MIN and $MAX, received $boardSize"
        }
    }
}

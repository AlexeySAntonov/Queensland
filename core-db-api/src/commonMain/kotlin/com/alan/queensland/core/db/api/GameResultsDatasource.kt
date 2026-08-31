package com.alan.queensland.core.db.api

import com.alan.queensland.core.db.api.model.GameResultModel
import com.alan.queensland.core.db.api.model.GameResultPieceType
import kotlinx.coroutines.flow.Flow

interface GameResultsDatasource {

    fun observeResults(): Flow<List<GameResultModel>>

    suspend fun saveResult(
        boardSize: Int,
        pieceType: GameResultPieceType,
        timeSpentMillis: Long,
        piecePositions: Set<Pair<Int, Int>>,
    )

    suspend fun deleteResult(uuid: String)
}

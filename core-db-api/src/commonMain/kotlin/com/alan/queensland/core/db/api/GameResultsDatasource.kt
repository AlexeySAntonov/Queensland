package com.alan.queensland.core.db.api

import com.alan.queensland.core.db.api.model.GameResultModel
import kotlinx.coroutines.flow.Flow

interface GameResultsDatasource {

    fun observeResults(): Flow<List<GameResultModel>>

    suspend fun saveResult(boardSize: Int, timeSpentMillis: Long)

    suspend fun deleteResult(uuid: String)
}

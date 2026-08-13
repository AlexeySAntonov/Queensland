package com.alan.queensland.leaderboard.impl.data

import com.alan.queensland.core.db.api.GameResultsDatasource
import com.alan.queensland.core.db.api.model.GameResultModel
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class LeaderBoardRepository(
    private val gameResultsDatasource: GameResultsDatasource,
) {
    fun observeResults(): Flow<List<GameResultModel>> = gameResultsDatasource.observeResults()

    suspend fun deleteResult(uuid: String) {
        gameResultsDatasource.deleteResult(uuid)
    }
}

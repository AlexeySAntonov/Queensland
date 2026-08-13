package com.alan.queensland.leaderboard.impl.business

import com.alan.queensland.core.db.api.model.GameResultModel
import com.alan.queensland.leaderboard.impl.data.LeaderBoardRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveLeaderBoardUseCase(
    private val repository: LeaderBoardRepository,
) {
    operator fun invoke(): Flow<List<GameResultModel>> = repository.observeResults()
}

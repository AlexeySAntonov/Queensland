package com.alan.queensland.leaderboard.impl.ui

import com.alan.queensland.core.db.api.model.GameResultModel
import com.alan.queensland.core.utils.time.formatElapsedTime
import me.tatarka.inject.annotations.Inject
import kotlin.time.Instant

@Inject
class LeaderBoardUiStateMapper {

    operator fun invoke(results: List<GameResultModel>) = LeaderBoardUiState(
        results = results.map { result ->
            GameResultUiState(
                uuid = result.uuid,
                boardSize = result.boardSize,
                formattedTimeSpent = formatElapsedTime(result.timeSpentMillis),
                completedAt = Instant.fromEpochMilliseconds(result.createdAtMillis).toString(),
            )
        },
    )
}

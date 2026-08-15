package com.alan.queensland.leaderboard.impl.ui

import com.alan.queensland.core.db.api.model.GameResultModel
import com.alan.queensland.core.ui.base.util.formatFullLocalDateTime
import com.alan.queensland.core.utils.time.formatElapsedTime
import me.tatarka.inject.annotations.Inject

@Inject
class LeaderBoardUiStateMapper {

    operator fun invoke(results: List<GameResultModel>) = LeaderBoardUiState(
        groups = results
            .groupBy(GameResultModel::boardSize)
            .entries
            .sortedBy(Map.Entry<Int, List<GameResultModel>>::key)
            .map { (boardSize, boardResults) ->
                LeaderBoardGroupUiState(
                    boardSize = boardSize,
                    results = boardResults
                        .sortedWith(
                            compareBy<GameResultModel>(GameResultModel::timeSpentMillis)
                                .thenByDescending(GameResultModel::createdAtMillis),
                        ).mapIndexed { index, result ->
                            GameResultUiState(
                                uuid = result.uuid,
                                rank = index + 1,
                                formattedTimeSpent = formatElapsedTime(result.timeSpentMillis),
                                formattedCompletedAt = formatFullLocalDateTime(result.createdAtMillis),
                            )
                        },
                )
            },
    )
}

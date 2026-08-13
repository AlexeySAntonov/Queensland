package com.alan.queensland.leaderboard.impl.business

import co.touchlab.kermit.Logger
import com.alan.queensland.core.utils.result.resultOf
import com.alan.queensland.leaderboard.impl.data.LeaderBoardRepository
import me.tatarka.inject.annotations.Inject

@Inject
class DeleteResultUseCase(
    private val repository: LeaderBoardRepository,
) {

    private val logger = Logger.withTag("DeleteResultUseCase")

    suspend operator fun invoke(uuid: String): Result<Unit> =
        resultOf {
            repository.deleteResult(uuid)
        }.onFailure { throwable ->
            logger.e(throwable) { "Failed to delete leaderboard result" }
        }
}

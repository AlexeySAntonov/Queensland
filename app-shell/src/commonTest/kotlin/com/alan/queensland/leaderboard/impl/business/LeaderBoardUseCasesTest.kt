package com.alan.queensland.leaderboard.impl.business

import com.alan.queensland.core.db.api.model.GameResultModel
import com.alan.queensland.core.db.test.FakeGameResultsDatasource
import com.alan.queensland.leaderboard.impl.data.LeaderBoardRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LeaderBoardUseCasesTest {

    @Test
    fun observesAndDeletesResultsThroughRepository() = runTest {
        val result = GameResultModel(
            uuid = "result-id",
            timeSpentMillis = 2_000L,
            createdAtMillis = 3_000L,
            boardSize = 4,
            queenPositions = emptySet(),
        )
        val datasource = FakeGameResultsDatasource(initialResults = listOf(result))
        val repository = LeaderBoardRepository(datasource)
        val observeLeaderBoard = ObserveLeaderBoardUseCase(repository)
        val deleteResult = DeleteResultUseCase(repository)

        assertEquals(listOf(result), observeLeaderBoard().first())

        assertTrue(deleteResult(result.uuid).isSuccess)

        assertEquals(listOf(result.uuid), datasource.deletedResultUuids)
        assertEquals(emptyList(), observeLeaderBoard().first())
    }
}

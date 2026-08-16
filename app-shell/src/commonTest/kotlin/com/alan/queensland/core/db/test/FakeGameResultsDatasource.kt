package com.alan.queensland.core.db.test

import com.alan.queensland.core.db.api.GameResultsDatasource
import com.alan.queensland.core.db.api.model.GameResultModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeGameResultsDatasource(
    initialResults: List<GameResultModel> = emptyList(),
) : GameResultsDatasource {

    private val results = MutableStateFlow(initialResults)

    val savedResults = mutableListOf<SavedGameResult>()
    val deletedResultUuids = mutableListOf<String>()
    var saveResultFailure: Throwable? = null

    override fun observeResults(): Flow<List<GameResultModel>> = results

    override suspend fun saveResult(
        boardSize: Int,
        timeSpentMillis: Long,
        queenPositions: Set<Pair<Int, Int>>,
    ) {
        saveResultFailure?.let { throwable -> throw throwable }
        savedResults += SavedGameResult(
            boardSize = boardSize,
            timeSpentMillis = timeSpentMillis,
            queenPositions = queenPositions,
        )
    }

    override suspend fun deleteResult(uuid: String) {
        deletedResultUuids += uuid
        results.value = results.value.filterNot { result -> result.uuid == uuid }
    }
}

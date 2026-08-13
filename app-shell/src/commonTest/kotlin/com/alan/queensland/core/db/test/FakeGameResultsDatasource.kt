package com.alan.queensland.core.db.test

import com.alan.queensland.core.db.api.GameResultsDatasource
import com.alan.queensland.core.db.api.model.GameResultModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeGameResultsDatasource(
    initialResults: List<GameResultModel> = emptyList(),
) : GameResultsDatasource {

    data class SavedResult(
        val boardSize: Int,
        val timeSpentMillis: Long,
    )

    private val results = MutableStateFlow(initialResults)

    val savedResults = mutableListOf<SavedResult>()
    val deletedResultUuids = mutableListOf<String>()

    override fun observeResults(): Flow<List<GameResultModel>> = results

    override suspend fun saveResult(
        boardSize: Int,
        timeSpentMillis: Long,
    ) {
        savedResults += SavedResult(
            boardSize = boardSize,
            timeSpentMillis = timeSpentMillis,
        )
    }

    override suspend fun deleteResult(uuid: String) {
        deletedResultUuids += uuid
        results.value = results.value.filterNot { result -> result.uuid == uuid }
    }
}

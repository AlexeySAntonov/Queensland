package com.alan.queensland.core.db.impl.data

import com.alan.queensland.core.db.api.GameResultsDatasource
import com.alan.queensland.core.db.api.model.GameResultModel
import com.alan.queensland.core.db.impl.entity.GameResultEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Inject
class GameResultsDatasourceImpl(
    private val db: QueenslandDatabase,
) : GameResultsDatasource {

    override fun observeResults(): Flow<List<GameResultModel>> {
        return db.gameResultDao().observeResults()
            .map { entities -> entities.map { entity -> entity.toModel() } }
    }

    override suspend fun saveResult(boardSize: Int, timeSpentMillis: Long) {
        db.gameResultDao().insert(
            GameResultEntity(
                uuid = Uuid.random().toString(),
                timeSpentMillis = timeSpentMillis,
                createdAtMillis = Clock.System.now().toEpochMilliseconds(),
                boardSize = boardSize,
            ),
        )
    }

    override suspend fun deleteResult(uuid: String) {
        db.gameResultDao().delete(uuid)
    }

    private fun GameResultEntity.toModel() = GameResultModel(
        uuid = uuid,
        timeSpentMillis = timeSpentMillis,
        createdAtMillis = createdAtMillis,
        boardSize = boardSize,
    )
}

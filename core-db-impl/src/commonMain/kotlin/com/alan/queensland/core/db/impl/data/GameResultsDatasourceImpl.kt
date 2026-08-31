package com.alan.queensland.core.db.impl.data

import com.alan.queensland.core.db.api.GameResultsDatasource
import com.alan.queensland.core.db.api.model.GameResultModel
import com.alan.queensland.core.db.api.model.GameResultPieceType
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

    override suspend fun saveResult(
        boardSize: Int,
        pieceType: GameResultPieceType,
        timeSpentMillis: Long,
        piecePositions: Set<Pair<Int, Int>>,
    ) {
        db.gameResultDao().insert(
            GameResultEntity(
                uuid = Uuid.random().toString(),
                timeSpentMillis = timeSpentMillis,
                createdAtMillis = Clock.System.now().toEpochMilliseconds(),
                boardSize = boardSize,
                pieceType = pieceType.name,
                piecePositions = piecePositions.encode(),
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
        pieceType = GameResultPieceType.valueOf(pieceType),
        piecePositions = piecePositions.decode(),
    )

    private fun Set<Pair<Int, Int>>.encode(): String {
        return joinToString(separator = PIECE_SEPARATOR.toString()) { position ->
            "${position.first}$COORDINATE_SEPARATOR${position.second}"
        }
    }

    private fun String.decode(): Set<Pair<Int, Int>> {
        if (isEmpty()) return emptySet()

        return split(PIECE_SEPARATOR).mapTo(mutableSetOf()) { position ->
            val (row, column) = position.split(COORDINATE_SEPARATOR, limit = 2)
            row.toInt() to column.toInt()
        }
    }

    private companion object {
        const val PIECE_SEPARATOR = ','
        const val COORDINATE_SEPARATOR = ':'
    }
}

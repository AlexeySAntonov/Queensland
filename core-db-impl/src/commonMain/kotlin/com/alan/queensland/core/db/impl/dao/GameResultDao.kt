package com.alan.queensland.core.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alan.queensland.core.db.impl.entity.GameResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {

    @Query("SELECT * FROM game_results ORDER BY createdAtMillis DESC")
    fun observeResults(): Flow<List<GameResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GameResultEntity)

    @Query("DELETE FROM game_results WHERE uuid = :uuid")
    suspend fun delete(uuid: String)
}

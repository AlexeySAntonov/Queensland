package com.alan.queensland.core.db.impl.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.alan.queensland.core.db.impl.dao.GameResultDao
import com.alan.queensland.core.db.impl.entity.GameResultEntity

@Database(
    entities = [
        GameResultEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@ConstructedBy(QueenslandDatabaseConstructor::class)
abstract class QueenslandDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao
}

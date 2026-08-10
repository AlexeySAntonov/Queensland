package com.alan.queensland.core.db.impl.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey val uuid: String,
    val timeSpentMillis: Long,
    val createdAtMillis: Long,
    val boardSize: Int,
)

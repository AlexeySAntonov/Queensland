package com.alan.queensland.core.db.impl.data

import androidx.room.RoomDatabaseConstructor

expect object QueenslandDatabaseConstructor : RoomDatabaseConstructor<QueenslandDatabase> {
    override fun initialize(): QueenslandDatabase
}

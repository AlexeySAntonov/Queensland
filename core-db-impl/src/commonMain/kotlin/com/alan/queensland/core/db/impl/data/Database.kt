package com.alan.queensland.core.db.impl.data

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

expect fun databaseBuilder(): RoomDatabase.Builder<QueenslandDatabase>

fun getRoomDatabase(): QueenslandDatabase {
    return databaseBuilder()
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

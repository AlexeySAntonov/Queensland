package com.alan.queensland.core.db.impl.data

import androidx.room.Room
import androidx.room.RoomDatabase
import com.alan.queensland.core.utils.context.AppContextProvider

actual fun databaseBuilder(): RoomDatabase.Builder<QueenslandDatabase> {
    val context = AppContextProvider.get()
    return Room.databaseBuilder<QueenslandDatabase>(
        context = context,
        name = context.getDatabasePath("queensland.db").absolutePath,
    )
}

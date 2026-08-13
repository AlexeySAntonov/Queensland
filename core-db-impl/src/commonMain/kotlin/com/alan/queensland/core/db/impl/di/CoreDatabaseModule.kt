package com.alan.queensland.core.db.impl.di

import com.alan.queensland.core.db.api.GameResultsDatasource
import com.alan.queensland.core.db.impl.data.GameResultsDatasourceImpl
import com.alan.queensland.core.db.impl.data.QueenslandDatabase
import com.alan.queensland.core.db.impl.data.getRoomDatabase
import com.alan.queensland.core.di.Singleton
import me.tatarka.inject.annotations.Provides

interface CoreDatabaseModule {

    @Provides
    @Singleton
    fun bindGameResultsDatasource(implementation: GameResultsDatasourceImpl): GameResultsDatasource = implementation

    @Provides
    @Singleton
    fun bindDatabase(): QueenslandDatabase = getRoomDatabase()
}

package com.alan.queensland.game.impl.di

import com.alan.queensland.core.di.Singleton
import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.game.impl.data.GameRepositoryImpl
import me.tatarka.inject.annotations.Provides

interface CoreGameModule {

    @Provides
    @Singleton
    fun bindGameRepository(repository: GameRepositoryImpl): GameRepository = repository
}

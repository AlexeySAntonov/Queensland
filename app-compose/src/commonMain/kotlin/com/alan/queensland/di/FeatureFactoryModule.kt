package com.alan.queensland.di

import com.alan.queensland.core.di.Singleton
import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.game.impl.di.GameComponent
import com.alan.queensland.home.impl.di.HomeComponent
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Provides

interface FeatureFactoryModule {

    @Provides
    @Singleton
    fun bindHomeDependencies(
        gameRepository: GameRepository,
        router: Router,
    ): HomeComponent.Dependencies {
        return object : HomeComponent.Dependencies {
            override fun gameRepository(): GameRepository = gameRepository
            override fun router(): Router = router
        }
    }

    @Provides
    @Singleton
    fun bindGameDependencies(
        gameRepository: GameRepository,
        router: Router,
    ): GameComponent.Dependencies {
        return object : GameComponent.Dependencies {
            override fun gameRepository(): GameRepository = gameRepository
            override fun router(): Router = router
        }
    }
}

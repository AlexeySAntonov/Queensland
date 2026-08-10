package com.alan.queensland.home.impl.di

import com.alan.queensland.core.di.FeatureScope
import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.home.impl.ui.HomeViewModel
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides

@Component
@FeatureScope
abstract class HomeComponent(
    @get:Provides val gameRepository: GameRepository,
    @get:Provides val router: Router,
) {
    abstract val homeViewModel: HomeViewModel

    interface Dependencies {
        fun gameRepository(): GameRepository
        fun router(): Router
    }

    companion object {
        fun init(dependencies: Dependencies): HomeComponent {
            return createKmp(
                dependencies.gameRepository(),
                dependencies.router(),
            )
        }
    }
}

@KmpComponentCreate
expect fun createKmp(
    gameRepository: GameRepository,
    router: Router,
): HomeComponent

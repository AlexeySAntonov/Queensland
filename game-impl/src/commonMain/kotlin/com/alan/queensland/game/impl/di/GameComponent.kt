package com.alan.queensland.game.impl.di

import com.alan.queensland.core.di.FeatureScope
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.game.impl.ui.GameViewModel
import com.alan.queensland.game.impl.ui.configuration.GameConfigurationViewModel
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides

@Component
@FeatureScope
abstract class GameComponent(
    @get:Provides val gameRepository: GameRepository,
    @get:Provides val router: Router,
    @get:Provides val coroutineDispatchers: CoroutineDispatchers,
) {

    abstract val gameConfigurationViewModel: GameConfigurationViewModel
    abstract val gameViewModel: GameViewModel

    interface Dependencies {
        fun gameRepository(): GameRepository
        fun router(): Router
        fun coroutineDispatchers(): CoroutineDispatchers
    }

    companion object {
        fun init(dependencies: Dependencies): GameComponent {
            return createKmp(
                dependencies.gameRepository(),
                dependencies.router(),
                dependencies.coroutineDispatchers(),
            )
        }
    }
}

@KmpComponentCreate
expect fun createKmp(
    gameRepository: GameRepository,
    router: Router,
    coroutineDispatchers: CoroutineDispatchers,
): GameComponent

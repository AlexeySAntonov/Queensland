package com.alan.queensland.leaderboard.impl.di

import com.alan.queensland.core.db.api.GameResultsDatasource
import com.alan.queensland.core.di.FeatureScope
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import com.alan.queensland.leaderboard.impl.ui.LeaderBoardViewModel
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides

@Component
@FeatureScope
abstract class LeaderBoardComponent(
    @get:Provides val gameResultsDatasource: GameResultsDatasource,
    @get:Provides val coroutineDispatchers: CoroutineDispatchers,
    @get:Provides val router: Router,
) {
    abstract val leaderBoardViewModel: LeaderBoardViewModel

    interface Dependencies {
        fun gameResultsDatasource(): GameResultsDatasource
        fun coroutineDispatchers(): CoroutineDispatchers
        fun router(): Router
    }

    companion object {
        fun init(dependencies: Dependencies): LeaderBoardComponent {
            return createKmp(
                dependencies.gameResultsDatasource(),
                dependencies.coroutineDispatchers(),
                dependencies.router(),
            )
        }
    }
}

@KmpComponentCreate
expect fun createKmp(
    gameResultsDatasource: GameResultsDatasource,
    coroutineDispatchers: CoroutineDispatchers,
    router: Router,
): LeaderBoardComponent

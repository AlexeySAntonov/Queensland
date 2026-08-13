package com.alan.queensland.leaderboard.impl.di

import com.alan.queensland.core.di.FeatureScope
import com.alan.queensland.leaderboard.impl.ui.LeaderBoardViewModel
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides

@Component
@FeatureScope
abstract class LeaderBoardComponent(
    @get:Provides val router: Router,
) {
    abstract val leaderBoardViewModel: LeaderBoardViewModel

    interface Dependencies {
        fun router(): Router
    }

    companion object {
        fun init(dependencies: Dependencies): LeaderBoardComponent {
            return createKmp(dependencies.router())
        }
    }
}

@KmpComponentCreate
expect fun createKmp(
    router: Router,
): LeaderBoardComponent

package com.alan.queensland.di

import com.alan.queensland.core.db.impl.di.CoreDatabaseModule
import com.alan.queensland.core.di.Singleton
import com.alan.queensland.game.impl.di.CoreGameModule
import com.alan.queensland.game.impl.di.GameComponent
import com.alan.queensland.home.impl.di.HomeComponent
import com.alan.queensland.leaderboard.impl.di.LeaderBoardComponent
import com.alan.queensland.navigation.api.Router
import com.alan.queensland.navigation.impl.di.CoreNavigationModule
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate

@Singleton
@Component
abstract class AppComponent :
    CoreDatabaseModule,
    CoreNavigationModule,
    CoreGameModule,
    DispatchersModule,
    FeatureFactoryModule {

    abstract fun router(): Router
    abstract fun gameDependencies(): GameComponent.Dependencies
    abstract fun homeDependencies(): HomeComponent.Dependencies
    abstract fun leaderBoardDependencies(): LeaderBoardComponent.Dependencies

    companion object {
        fun init(): AppComponent = createKmp()
    }
}

@KmpComponentCreate
expect fun createKmp(): AppComponent

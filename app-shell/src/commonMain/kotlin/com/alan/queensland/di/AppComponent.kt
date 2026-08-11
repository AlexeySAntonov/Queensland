package com.alan.queensland.di

import com.alan.queensland.core.db.impl.data.QueenslandDatabase
import com.alan.queensland.core.db.impl.di.CoreDatabaseModule
import com.alan.queensland.core.di.Singleton
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import com.alan.queensland.game.api.GameRepository
import com.alan.queensland.game.impl.di.CoreGameModule
import com.alan.queensland.game.impl.di.GameComponent
import com.alan.queensland.home.impl.di.HomeComponent
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

    abstract fun coroutineDispatchers(): CoroutineDispatchers
    abstract fun database(): QueenslandDatabase
    abstract fun gameRepository(): GameRepository
    abstract fun router(): Router
    abstract fun gameDependencies(): GameComponent.Dependencies
    abstract fun homeDependencies(): HomeComponent.Dependencies

    companion object {
        fun init(): AppComponent = createKmp()
    }
}

@KmpComponentCreate
expect fun createKmp(): AppComponent

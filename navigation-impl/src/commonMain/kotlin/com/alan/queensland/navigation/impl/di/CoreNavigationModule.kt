package com.alan.queensland.navigation.impl.di

import com.alan.queensland.core.di.Singleton
import com.alan.queensland.navigation.api.Router
import com.alan.queensland.navigation.impl.RouterImpl
import me.tatarka.inject.annotations.Provides

interface CoreNavigationModule {

    @Provides
    @Singleton
    fun bindRouter(router: RouterImpl): Router = router
}

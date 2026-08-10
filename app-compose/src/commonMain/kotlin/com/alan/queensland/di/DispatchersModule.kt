package com.alan.queensland.di

import com.alan.queensland.core.di.DispatcherDefault
import com.alan.queensland.core.di.DispatcherIO
import com.alan.queensland.core.di.DispatcherMain
import com.alan.queensland.core.di.Singleton
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Provides

interface DispatchersModule {

    @Provides
    @Singleton
    fun bindMainDispatcher(): @DispatcherMain CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    fun bindIODispatcher(): @DispatcherIO CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun bindDefaultDispatcher(): @DispatcherDefault CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    fun bindCoroutineDispatchers(
        @DispatcherMain main: CoroutineDispatcher,
        @DispatcherIO io: CoroutineDispatcher,
        @DispatcherDefault default: CoroutineDispatcher,
    ): CoroutineDispatchers {
        return CoroutineDispatchers(
            dispatcherMain = main,
            dispatcherIo = io,
            dispatcherDefault = default,
        )
    }
}

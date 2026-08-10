package com.alan.queensland

import android.app.Application
import com.alan.queensland.core.utils.context.AppContextProvider
import com.alan.queensland.di.DI

class QueenslandApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContextProvider.init(this)
        DI.init()
    }
}

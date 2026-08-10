package com.alan.queensland.core.utils.context

import android.content.Context

object AppContextProvider {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun get(): Context = appContext
}

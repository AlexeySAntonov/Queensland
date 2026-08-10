package com.alan.queensland.di

object DI {

    @Volatile
    lateinit var appComponent: AppComponent
        private set

    fun init() {
        if (!this::appComponent.isInitialized) {
            synchronized(this) {
                if (!this::appComponent.isInitialized) {
                    appComponent = AppComponent.init()
                }
            }
        }
    }
}

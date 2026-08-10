package com.alan.queensland.home.impl.di

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

object HomeComponentHolder : SynchronizedObject() {
    private var component: HomeComponent? = null

    fun get(dependencies: HomeComponent.Dependencies): HomeComponent {
        if (component == null) {
            synchronized(this) {
                if (component == null) {
                    component = HomeComponent.init(dependencies)
                }
            }
        }
        return requireNotNull(component)
    }

    fun reset() {
        component = null
    }
}

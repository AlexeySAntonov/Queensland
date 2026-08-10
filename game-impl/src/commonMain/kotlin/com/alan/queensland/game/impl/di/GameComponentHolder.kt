package com.alan.queensland.game.impl.di

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

object GameComponentHolder : SynchronizedObject() {
    private var component: GameComponent? = null

    fun get(dependencies: GameComponent.Dependencies): GameComponent {
        if (component == null) {
            synchronized(this) {
                if (component == null) {
                    component = GameComponent.init(dependencies)
                }
            }
        }
        return requireNotNull(component)
    }

    fun reset() {
        component = null
    }
}

package com.alan.queensland.leaderboard.impl.di

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

object LeaderBoardComponentHolder : SynchronizedObject() {
    private var component: LeaderBoardComponent? = null

    fun get(dependencies: LeaderBoardComponent.Dependencies): LeaderBoardComponent {
        if (component == null) {
            synchronized(this) {
                if (component == null) {
                    component = LeaderBoardComponent.init(dependencies)
                }
            }
        }
        return requireNotNull(component)
    }

    fun reset() {
        component = null
    }
}

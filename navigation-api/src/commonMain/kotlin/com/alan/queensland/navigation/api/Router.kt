package com.alan.queensland.navigation.api

import kotlinx.coroutines.flow.Flow

interface Router {
    val events: Flow<NavigationEvent>

    fun openGameConfiguration()
    fun openGame()
    fun openResults()
    fun back()
}

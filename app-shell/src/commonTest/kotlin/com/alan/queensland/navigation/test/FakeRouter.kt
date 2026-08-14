package com.alan.queensland.navigation.test

import com.alan.queensland.navigation.api.NavigationEvent
import com.alan.queensland.navigation.api.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class FakeRouter : Router {

    override val events: Flow<NavigationEvent> = emptyFlow()

    val sentEvents = mutableListOf<NavigationEvent>()

    override fun openGameConfiguration() {
        sentEvents += NavigationEvent.OpenGameConfiguration
    }

    override fun openGame() {
        sentEvents += NavigationEvent.OpenGame
    }

    override fun openGameFinished() {
        sentEvents += NavigationEvent.OpenGameFinished
    }

    override fun openLeaderBoard() {
        sentEvents += NavigationEvent.OpenLeaderBoard
    }

    override fun back() {
        sentEvents += NavigationEvent.Back
    }
}

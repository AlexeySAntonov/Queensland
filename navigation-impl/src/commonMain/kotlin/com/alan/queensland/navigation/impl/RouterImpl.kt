package com.alan.queensland.navigation.impl

import com.alan.queensland.navigation.api.NavigationEvent
import com.alan.queensland.navigation.api.Router
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import me.tatarka.inject.annotations.Inject

@Inject
class RouterImpl : Router {
    private val eventsChannel = Channel<NavigationEvent>(Channel.BUFFERED)

    override val events: Flow<NavigationEvent> = eventsChannel.receiveAsFlow() // TODO handle double clicks

    override fun openGameConfiguration() {
        eventsChannel.trySend(NavigationEvent.OpenGameConfiguration)
    }

    override fun openGame() {
        eventsChannel.trySend(NavigationEvent.OpenGame)
    }

    override fun openResults() {
        eventsChannel.trySend(NavigationEvent.OpenResults)
    }

    override fun back() {
        eventsChannel.trySend(NavigationEvent.Back)
    }
}

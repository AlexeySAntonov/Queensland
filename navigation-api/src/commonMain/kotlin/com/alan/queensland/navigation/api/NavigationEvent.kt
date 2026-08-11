package com.alan.queensland.navigation.api

sealed interface NavigationEvent {
    data object OpenGameConfiguration : NavigationEvent
    data object OpenGame : NavigationEvent
    data object OpenResults : NavigationEvent
    data object Back : NavigationEvent
}

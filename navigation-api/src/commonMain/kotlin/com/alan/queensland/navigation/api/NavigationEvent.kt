package com.alan.queensland.navigation.api

sealed interface NavigationEvent {
    data object OpenGameConfiguration : NavigationEvent
    data object OpenGame : NavigationEvent
    data object OpenLeaderBoard : NavigationEvent
    data object Back : NavigationEvent
}

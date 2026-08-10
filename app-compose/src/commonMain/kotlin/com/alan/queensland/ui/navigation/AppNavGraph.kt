package com.alan.queensland.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.alan.queensland.di.AppComponent
import com.alan.queensland.game.impl.di.GameComponentHolder
import com.alan.queensland.home.impl.di.HomeComponentHolder
import com.alan.queensland.navigation.api.NavigationEvent
import com.alan.queensland.navigation.api.Screens
import com.alan.queensland.ui.navigation.nodes.gameNode
import com.alan.queensland.ui.navigation.nodes.homeNode
import com.alan.queensland.ui.navigation.nodes.resultsNode

@Composable
fun AppNavGraph(
    appComponent: AppComponent,
) {
    val navController = rememberNavController()
    val router = appComponent.router()

    LaunchedEffect(router) {
        router.events.collect { event ->
            when (event) {
                NavigationEvent.OpenGame -> navController.navigate(Screens.Game.route)
                NavigationEvent.OpenResults -> navController.navigate(Screens.Results.route)
                NavigationEvent.Back -> navController.popBackStack()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route,
    ) {
        homeNode { HomeComponentHolder.get(appComponent.homeDependencies()) }
        gameNode { GameComponentHolder.get(appComponent.gameDependencies()) }
        resultsNode { GameComponentHolder.get(appComponent.gameDependencies()) }
    }
}

package com.alan.queensland.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.alan.queensland.di.AppComponent
import com.alan.queensland.game.impl.di.GameComponentHolder
import com.alan.queensland.home.impl.di.HomeComponentHolder
import com.alan.queensland.leaderboard.impl.di.LeaderBoardComponentHolder
import com.alan.queensland.navigation.api.NavigationEvent
import com.alan.queensland.navigation.impl.Screens
import com.alan.queensland.ui.navigation.nodes.gameNode
import com.alan.queensland.ui.navigation.nodes.gameConfigurationNode
import com.alan.queensland.ui.navigation.nodes.homeNode
import com.alan.queensland.ui.navigation.nodes.leaderBoardNode

@Composable
fun AppNavGraph(
    appComponent: AppComponent,
) {
    val navController = rememberNavController()
    val router = appComponent.router()

    LaunchedEffect(router) {
        router.events.collect { event ->
            when (event) {
                NavigationEvent.OpenGameConfiguration -> navController.openGameConfiguration()
                NavigationEvent.OpenGame -> navController.openGame()
                NavigationEvent.OpenLeaderBoard -> navController.openLeaderBoard()
                NavigationEvent.Back -> navController.popBackStack()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route,
    ) {
        homeNode { HomeComponentHolder.get(appComponent.homeDependencies()) }
        gameConfigurationNode { GameComponentHolder.get(appComponent.gameDependencies()) }
        gameNode { GameComponentHolder.get(appComponent.gameDependencies()) }
        leaderBoardNode { LeaderBoardComponentHolder.get(appComponent.leaderBoardDependencies()) }
    }
}

private fun NavHostController.openGameConfiguration() = navigate(Screens.GameConfiguration.route)

private fun NavHostController.openGame() {
    val fromConfiguration = currentDestination?.route == Screens.GameConfiguration.route

    navigate(Screens.Game.route) {
        // Consider switching to a sub-navgraph if configuration gets additional steps.
        if (fromConfiguration) {
            popUpTo(Screens.GameConfiguration.route) {
                inclusive = true
            }
        }
    }
}

private fun NavHostController.openLeaderBoard() = navigate(Screens.LeaderBoard.route)

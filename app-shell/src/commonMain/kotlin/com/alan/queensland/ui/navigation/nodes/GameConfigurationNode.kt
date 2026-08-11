package com.alan.queensland.ui.navigation.nodes

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alan.queensland.game.impl.di.GameComponent
import com.alan.queensland.game.impl.ui.configuration.GameConfigurationScreen
import com.alan.queensland.navigation.impl.Screens

fun NavGraphBuilder.gameConfigurationNode(
    componentCreator: () -> GameComponent,
) {
    composable(
        route = Screens.GameConfiguration.route,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } },
    ) {
        val viewModel = viewModel { componentCreator().gameConfigurationViewModel }
        GameConfigurationScreen(viewModel = viewModel)
    }
}

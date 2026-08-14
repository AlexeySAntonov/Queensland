package com.alan.queensland.ui.navigation.nodes

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alan.queensland.game.impl.di.GameComponent
import com.alan.queensland.game.impl.ui.finished.GameFinishedScreen
import com.alan.queensland.navigation.impl.Screens

fun NavGraphBuilder.gameFinishedNode(
    componentCreator: () -> GameComponent,
) {
    composable(
        route = Screens.GameFinished.route,
        enterTransition = { slideInVertically { it } },
        exitTransition = { slideOutVertically { it } },
        popEnterTransition = { slideInVertically { it } },
        popExitTransition = { slideOutVertically { it } },
    ) {
        val viewModel = viewModel { componentCreator().gameFinishedViewModel }
        GameFinishedScreen(viewModel = viewModel)
    }
}

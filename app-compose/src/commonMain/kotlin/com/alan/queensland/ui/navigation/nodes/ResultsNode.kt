package com.alan.queensland.ui.navigation.nodes

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alan.queensland.game.impl.di.GameComponent
import com.alan.queensland.game.impl.ui.results.ResultsScreen
import com.alan.queensland.navigation.api.Screens

fun NavGraphBuilder.resultsNode(
    componentCreator: () -> GameComponent,
) {
    composable(
        route = Screens.Results.route,
        enterTransition = { slideInVertically { it } },
        exitTransition = { slideOutVertically { it } },
        popEnterTransition = { slideInVertically { it } },
        popExitTransition = { slideOutVertically { it } },
    ) {
        val viewModel = viewModel { componentCreator().resultsViewModel }
        ResultsScreen(viewModel = viewModel)
    }
}

package com.alan.queensland.ui.navigation.nodes

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alan.queensland.leaderboard.impl.di.LeaderBoardComponent
import com.alan.queensland.leaderboard.impl.ui.LeaderBoardScreen
import com.alan.queensland.navigation.impl.Screens

fun NavGraphBuilder.leaderBoardNode(
    componentCreator: () -> LeaderBoardComponent,
) {
    composable(
        route = Screens.LeaderBoard.route,
        enterTransition = { slideInVertically { it } },
        exitTransition = { slideOutVertically { it } },
        popEnterTransition = { slideInVertically { it } },
        popExitTransition = { slideOutVertically { it } },
    ) {
        val viewModel = viewModel { componentCreator().leaderBoardViewModel }
        LeaderBoardScreen(viewModel = viewModel)
    }
}

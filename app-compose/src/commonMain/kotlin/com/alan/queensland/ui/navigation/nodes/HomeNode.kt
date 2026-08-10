package com.alan.queensland.ui.navigation.nodes

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alan.queensland.home.impl.di.HomeComponent
import com.alan.queensland.home.impl.ui.HomeScreen
import com.alan.queensland.navigation.api.Screens

fun NavGraphBuilder.homeNode(
    componentCreator: () -> HomeComponent,
) {
    composable(Screens.Home.route) {
        val viewModel = viewModel { componentCreator().homeViewModel }
        HomeScreen(viewModel = viewModel)
    }
}

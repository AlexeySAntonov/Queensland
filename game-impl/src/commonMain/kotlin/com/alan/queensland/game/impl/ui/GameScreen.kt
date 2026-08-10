package com.alan.queensland.game.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings

@Composable
fun GameScreen(
    viewModel: GameViewModel,
) {
    val hasActiveGame by viewModel.hasActiveGame.collectAsState()

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Game",
                navigationIcon = Icons.AutoMirrored.Rounded.ArrowBack,
                navigationIconContentDescription = "Back",
                onNavigationClick = viewModel::onBackClick,
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(Paddings.one),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Queensland",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "N-Queens puzzle",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = Paddings.half),
            )
            Text(
                text = if (hasActiveGame) "Game in progress" else "No active game",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = Paddings.one),
            )
            Text(
                text = "Win results will be stored after the puzzle flow is implemented.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = Paddings.half),
            )
        }
    }
}

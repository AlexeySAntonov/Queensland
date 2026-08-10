package com.alan.queensland.home.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alan.queensland.core.ui.base.compose.themes.Paddings

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
) {
    val hasActiveGame by viewModel.hasActiveGame.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(Paddings.one),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Queensland",
            style = MaterialTheme.typography.headlineMedium,
        )
        Column(
            modifier = Modifier
                .padding(top = Paddings.one)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Paddings.half),
        ) {
            Button(
                onClick = viewModel::onNewGameClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("New game")
            }
            if (hasActiveGame) {
                OutlinedButton(
                    onClick = viewModel::onResumeGameClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Resume game")
                }
            }
            OutlinedButton(
                onClick = viewModel::onSeeResultsClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("See results")
            }
        }
    }
}

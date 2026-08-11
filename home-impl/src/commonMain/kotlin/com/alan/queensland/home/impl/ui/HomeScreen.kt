package com.alan.queensland.home.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alan.queensland.core.ui.base.compose.components.AppButton
import com.alan.queensland.core.ui.base.compose.themes.Paddings

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
) {
    val hasActiveGame by viewModel.hasActiveGame.collectAsStateWithLifecycle()

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
            AppButton(
                text = "New game",
                onClick = viewModel::onNewGameClick,
                modifier = Modifier.fillMaxWidth(),
            )
            if (hasActiveGame) {
                AppButton(
                    text = "Resume game",
                    onClick = viewModel::onResumeGameClick,
                    modifier = Modifier.fillMaxWidth(),
                    isOutlined = true,
                )
            }
            AppButton(
                text = "See results",
                onClick = viewModel::onSeeResultsClick,
                modifier = Modifier.fillMaxWidth(),
                isOutlined = true,
            )
        }
    }
}

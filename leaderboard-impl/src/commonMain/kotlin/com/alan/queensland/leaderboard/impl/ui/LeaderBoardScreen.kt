package com.alan.queensland.leaderboard.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import com.alan.queensland.core.ui.base.model.UiState

@Composable
fun LeaderBoardScreen(
    viewModel: LeaderBoardViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Leaderboard",
                navigationIcon = Icons.Default.Close,
                navigationIconContentDescription = "Back",
                onNavigationClick = viewModel::onBackClick,
            )
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(Paddings.one),
        ) {
            LeaderBoardContent(
                uiState = uiState,
                onDeleteResultClick = viewModel::onDeleteResultClick,
            )
        }
    }
}

@Composable
private fun BoxScope.LeaderBoardContent(
    uiState: UiState<LeaderBoardUiState>,
    onDeleteResultClick: (String) -> Unit,
) {
    when (uiState) {
        UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        UiState.Error -> Text(
            text = "Unable to load leaderboard",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge,
        )

        is UiState.Data -> LeaderBoardResults(
            state = uiState.value,
            onDeleteResultClick = onDeleteResultClick,
        )
    }
}

@Composable
private fun BoxScope.LeaderBoardResults(
    state: LeaderBoardUiState,
    onDeleteResultClick: (String) -> Unit,
) {
    if (state.results.isEmpty()) {
        Text(
            text = "No completed games yet",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge,
        )
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = state.results,
            key = GameResultUiState::uuid,
        ) { result ->
            GameResultRow(
                result = result,
                onDeleteClick = { onDeleteResultClick(result.uuid) },
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun GameResultRow(
    result: GameResultUiState,
    onDeleteClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(text = "${result.boardSize} x ${result.boardSize} board")
        },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(Paddings.half)) {
                Text(text = "Solved in ${result.formattedTimeSpent}")
                Text(
                    text = result.completedAt,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        trailingContent = {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete result",
                )
            }
        },
    )
}

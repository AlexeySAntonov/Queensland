package com.alan.queensland.game.impl.ui.configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alan.queensland.core.ui.base.compose.components.AppButton
import com.alan.queensland.core.ui.base.compose.components.AppChessBoard
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import com.alan.queensland.core.ui.base.util.GameBoardSize

@Composable
fun GameConfigurationScreen(
    viewModel: GameConfigurationViewModel,
) {
    val boardSize by viewModel.boardSize.collectAsState()

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Game configuration",
                navigationIcon = Icons.AutoMirrored.Rounded.ArrowBack,
                navigationIconContentDescription = "Back",
                onNavigationClick = viewModel::onBackClick,
            )
        },
    ) { contentPadding ->
        // NB: landscape requires different layout for proper presentation
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = Paddings.two, vertical = Paddings.one),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppChessBoard(
                boardSize = boardSize,
                modifier = Modifier
                    .padding(top = Paddings.one)
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.weight(1f))
            BoardSizeSelector(
                boardSize = boardSize,
                onDecreaseClick = viewModel::onDecreaseBoardSizeClick,
                onIncreaseClick = viewModel::onIncreaseBoardSizeClick,
            )
            Spacer(modifier = Modifier.weight(1f))
            AppButton(
                text = "Continue",
                onClick = viewModel::onContinueClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun BoardSizeSelector(
    boardSize: Int,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        OutlinedIconButton(
            onClick = onDecreaseClick,
            enabled = boardSize > GameBoardSize.MIN,
            modifier = Modifier.size(64.dp),
        ) {
            Text(
                text = "-",
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = boardSize.toString(),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        OutlinedIconButton(
            onClick = onIncreaseClick,
            enabled = boardSize < GameBoardSize.MAX,
            modifier = Modifier.size(64.dp),
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

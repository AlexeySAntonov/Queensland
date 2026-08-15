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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alan.queensland.core.ui.base.compose.components.AppButton
import com.alan.queensland.core.ui.base.compose.components.AppChessBoard
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import com.alan.queensland.core.ui.base.util.GameBoardSize
import org.jetbrains.compose.resources.stringResource
import queensland.game_impl.generated.resources.Res
import queensland.game_impl.generated.resources.board_size_decrease_symbol
import queensland.game_impl.generated.resources.board_size_increase_symbol
import queensland.game_impl.generated.resources.game_configuration_title
import queensland.game_impl.generated.resources.navigation_back
import queensland.game_impl.generated.resources.start_game

@Composable
fun GameConfigurationScreen(
    viewModel: GameConfigurationViewModel,
) {
    val boardSize by viewModel.boardSize.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.game_configuration_title),
                navigationIcon = Icons.AutoMirrored.Rounded.ArrowBack,
                navigationIconContentDescription = stringResource(Res.string.navigation_back),
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
                text = stringResource(Res.string.start_game),
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
                text = stringResource(Res.string.board_size_decrease_symbol),
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
                text = stringResource(Res.string.board_size_increase_symbol),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

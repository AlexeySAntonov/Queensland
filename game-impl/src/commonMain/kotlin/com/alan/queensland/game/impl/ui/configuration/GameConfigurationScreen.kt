package com.alan.queensland.game.impl.ui.configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alan.queensland.core.ui.base.FormFactor
import com.alan.queensland.core.ui.base.compose.components.AppButton
import com.alan.queensland.core.ui.base.compose.components.AppChessBoard
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import com.alan.queensland.game.api.GameBoardSize
import com.alan.queensland.game.api.PieceType
import com.alan.queensland.game.impl.ui.label
import org.jetbrains.compose.resources.stringResource
import queensland.game_impl.generated.resources.Res
import queensland.game_impl.generated.resources.board_size_decrease
import queensland.game_impl.generated.resources.board_size_decrease_symbol
import queensland.game_impl.generated.resources.board_size_increase
import queensland.game_impl.generated.resources.board_size_increase_symbol
import queensland.game_impl.generated.resources.board_size_selected
import queensland.game_impl.generated.resources.game_configuration_title
import queensland.game_impl.generated.resources.navigation_back
import queensland.game_impl.generated.resources.start_game

@Composable
fun GameConfigurationScreen(
    viewModel: GameConfigurationViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isWideScreen = FormFactor.isWideScreen()

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
        val modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = Paddings.two, vertical = Paddings.one)

        if (isWideScreen) {
            GameConfigurationWideScreen(
                state = uiState,
                onDecreaseBoardSizeClick = viewModel::onDecreaseBoardSizeClick,
                onIncreaseBoardSizeClick = viewModel::onIncreaseBoardSizeClick,
                onPieceTypeClick = viewModel::onPieceTypeClick,
                onContinueClick = viewModel::onContinueClick,
                modifier = modifier,
            )
        } else {
            GameConfigurationPhoneScreen(
                state = uiState,
                onDecreaseBoardSizeClick = viewModel::onDecreaseBoardSizeClick,
                onIncreaseBoardSizeClick = viewModel::onIncreaseBoardSizeClick,
                onPieceTypeClick = viewModel::onPieceTypeClick,
                onContinueClick = viewModel::onContinueClick,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun GameConfigurationWideScreen(
    state: GameConfigurationUiState,
    onDecreaseBoardSizeClick: () -> Unit,
    onIncreaseBoardSizeClick: () -> Unit,
    onPieceTypeClick: (PieceType) -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = Paddings.one),
            contentAlignment = Alignment.Center,
        ) {
            val boardDimension = minOf(maxWidth, maxHeight)
            AppChessBoard(
                boardSize = state.boardSize,
                modifier = Modifier.size(boardDimension),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GameConfigurationControls(
                state = state,
                onDecreaseBoardSizeClick = onDecreaseBoardSizeClick,
                onIncreaseBoardSizeClick = onIncreaseBoardSizeClick,
                onPieceTypeClick = onPieceTypeClick,
                onContinueClick = onContinueClick,
            )
        }
    }
}

@Composable
private fun GameConfigurationPhoneScreen(
    state: GameConfigurationUiState,
    onDecreaseBoardSizeClick: () -> Unit,
    onIncreaseBoardSizeClick: () -> Unit,
    onPieceTypeClick: (PieceType) -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppChessBoard(
            boardSize = state.boardSize,
            modifier = Modifier
                .padding(top = Paddings.one)
                .fillMaxWidth(),
        )
        GameConfigurationControls(
            state = state,
            onDecreaseBoardSizeClick = onDecreaseBoardSizeClick,
            onIncreaseBoardSizeClick = onIncreaseBoardSizeClick,
            onPieceTypeClick = onPieceTypeClick,
            onContinueClick = onContinueClick,
        )
    }
}

@Composable
private fun ColumnScope.GameConfigurationControls(
    state: GameConfigurationUiState,
    onDecreaseBoardSizeClick: () -> Unit,
    onIncreaseBoardSizeClick: () -> Unit,
    onPieceTypeClick: (PieceType) -> Unit,
    onContinueClick: () -> Unit,
) {
    Spacer(modifier = Modifier.weight(1f))
    BoardSizeSelector(
        boardSize = state.boardSize,
        onDecreaseClick = onDecreaseBoardSizeClick,
        onIncreaseClick = onIncreaseBoardSizeClick,
    )
    Spacer(modifier = Modifier.weight(1f))
    PieceTypeSelector(
        selectedPieceType = state.selectedPieceType,
        onPieceTypeClick = onPieceTypeClick,
    )
    Spacer(modifier = Modifier.weight(1f))
    AppButton(
        text = stringResource(Res.string.start_game),
        onClick = onContinueClick,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun BoardSizeSelector(
    boardSize: Int,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit,
) {
    val decreaseBoardSizeDescription = stringResource(Res.string.board_size_decrease)
    val increaseBoardSizeDescription = stringResource(Res.string.board_size_increase)
    val selectedBoardSizeDescription = stringResource(Res.string.board_size_selected, boardSize)

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
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = decreaseBoardSizeDescription
                },
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        Box(
            modifier = Modifier
                .size(64.dp)
                .clearAndSetSemantics {
                    contentDescription = selectedBoardSizeDescription
                },
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
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = increaseBoardSizeDescription
                },
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Composable
private fun PieceTypeSelector(
    selectedPieceType: PieceType,
    onPieceTypeClick: (PieceType) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(Paddings.half),
    ) {
        PieceType.entries.forEach { pieceType ->
            val isSelected = pieceType == selectedPieceType
            AppButton(
                text = pieceType.label(),
                onClick = { onPieceTypeClick(pieceType) },
                modifier = Modifier
                    .weight(1f)
                    .semantics { selected = isSelected },
                isOutlined = !isSelected,
            )
        }
    }
}

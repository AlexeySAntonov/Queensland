package com.alan.queensland.game.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.alan.queensland.core.ui.base.compose.components.AppChessBoard
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import com.alan.queensland.game.api.BoardPosition

@Composable
fun GameScreen(
    viewModel: GameViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

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
                .padding(horizontal = Paddings.one, vertical = Paddings.half),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val state = uiState
            if (state == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No active game",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else {
                QueenReserve(
                    remainingQueenCount = state.remainingQueenCount,
                    modifier = Modifier.fillMaxWidth(),
                )
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    val boardDimension = minOf(maxWidth, maxHeight)
                    AppChessBoard(
                        boardSize = state.boardSize,
                        modifier = Modifier.size(boardDimension),
                        onCellClick = viewModel::onCellClick,
                    ) { row, column ->
                        QueenCell(
                            position = BoardPosition(row = row, column = column),
                            state = state,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QueenReserve(
    remainingQueenCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(40.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 2.dp,
            alignment = Alignment.End,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(remainingQueenCount) {
            Box(
                modifier = Modifier.size(width = 20.dp, height = 28.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = QUEEN_SYMBOL,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}

@Composable
private fun BoxScope.QueenCell(
    position: BoardPosition,
    state: GameUiState,
) {
    if (position !in state.queenPositions) return

    val isConflicting = position in state.conflictingPositions
    if (isConflicting) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.errorContainer),
        )
    }
    Text(
        text = QUEEN_SYMBOL,
        modifier = Modifier.align(Alignment.Center),
        color = queenColor(position = position, isConflicting = isConflicting),
        style = queenTextStyle(state.boardSize),
        maxLines = 1,
    )
}

@Composable
private fun queenColor(
    position: BoardPosition,
    isConflicting: Boolean,
): Color = when {
    isConflicting -> MaterialTheme.colorScheme.onErrorContainer
    (position.row + position.column) % 2 == 0 -> MaterialTheme.colorScheme.onSurfaceVariant
    else -> MaterialTheme.colorScheme.onPrimary
}

@Composable
private fun queenTextStyle(boardSize: Int): TextStyle = when {
    boardSize <= 5 -> MaterialTheme.typography.displaySmall
    boardSize <= 8 -> MaterialTheme.typography.headlineLarge
    else -> MaterialTheme.typography.titleLarge
}

private const val QUEEN_SYMBOL = "♛"

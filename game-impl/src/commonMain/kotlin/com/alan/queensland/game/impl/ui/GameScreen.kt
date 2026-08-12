package com.alan.queensland.game.impl.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alan.queensland.core.ui.base.compose.components.AppButton
import com.alan.queensland.core.ui.base.compose.components.AppChessBoard
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import com.alan.queensland.core.ui.base.model.UiState
import com.alan.queensland.game.api.BoardPosition

@Composable
fun GameScreen(
    viewModel: GameViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(viewModel) {
        viewModel.onScreenResumed()
        onPauseOrDispose { viewModel.onScreenPaused() }
    }

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            GameUiStateContent(
                uiState = uiState,
                onCellClick = viewModel::onCellClick,
                onResetGameClick = viewModel::onResetGameClick,
            )
        }
    }
}

@Composable
private fun BoxScope.GameUiStateContent(
    uiState: UiState<GameUiState>,
    onCellClick: (row: Int, column: Int) -> Unit,
    onResetGameClick: () -> Unit,
) {
    when (uiState) {
        UiState.Loading -> LoadingContent()
        UiState.Error -> ErrorContent()
        is UiState.Data -> GameContent(
            state = uiState.value,
            onCellClick = onCellClick,
            onResetGameClick = onResetGameClick,
        )
    }
}

@Composable
private fun BoxScope.LoadingContent() {
    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
}

@Composable
private fun BoxScope.ErrorContent() {
    Text(
        text = "No active game", // TODO use resources
        modifier = Modifier.align(Alignment.Center),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onCellClick: (row: Int, column: Int) -> Unit,
    onResetGameClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Paddings.two, vertical = Paddings.one),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        GameTimer(formattedTimeSpent = state.formattedTimeSpent)
        Spacer(modifier = Modifier.weight(1f))
        QueenReserve(remainingQueenCount = state.remainingQueenCount)
        AppChessBoard(
            boardSize = state.boardSize,
            modifier = Modifier.fillMaxWidth(),
            onCellClick = onCellClick,
        ) { row, column ->
            QueenCell(
                position = BoardPosition(row = row, column = column),
                state = state,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            text = "Reset game",
            onClick = onResetGameClick,
            modifier = Modifier.fillMaxWidth(),
            isOutlined = true,
        )
    }
}

@Composable
private fun GameTimer(
    formattedTimeSpent: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        contentAlignment = Alignment.Center,
    ) {
        val textStyle = MaterialTheme.typography.displayMedium.copy(
            fontFamily = FontFamily.Monospace,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            formattedTimeSpent.forEachIndexed { index, character ->
                key(index) {
                    if (character == TIME_SEPARATOR) {
                        Text(
                            text = character.toString(),
                            modifier = Modifier.width(16.dp),
                            textAlign = TextAlign.Center,
                            style = textStyle,
                            maxLines = 1,
                        )
                    } else {
                        AnimatedTimerDigit(
                            digit = character,
                            index = index,
                            textStyle = textStyle,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedTimerDigit(
    digit: Char,
    index: Int,
    textStyle: TextStyle,
) {
    AnimatedContent(
        targetState = digit,
        modifier = Modifier.width(32.dp),
        transitionSpec = {
            (slideInVertically { height -> height / 2 } + fadeIn()) togetherWith
                (slideOutVertically { height -> -height / 2 } + fadeOut())
        },
        contentAlignment = Alignment.Center,
        label = "Game timer digit $index",
    ) { value ->
        Text(
            text = value.toString(),
            textAlign = TextAlign.Center,
            style = textStyle,
            maxLines = 1,
        )
    }
}

@Composable
private fun ColumnScope.QueenReserve(
    remainingQueenCount: Int,
) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .align(Alignment.End),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
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
private const val TIME_SEPARATOR = ':'

package com.alan.queensland.game.impl.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import com.alan.queensland.core.ui.base.compose.components.AppQueen
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import com.alan.queensland.core.ui.base.model.UiState
import org.jetbrains.compose.resources.stringResource
import queensland.game_impl.generated.resources.Res
import queensland.game_impl.generated.resources.game_not_active
import queensland.game_impl.generated.resources.game_screen_title
import queensland.game_impl.generated.resources.navigation_back
import queensland.game_impl.generated.resources.reset_game

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
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.game_screen_title),
                navigationIcon = Icons.AutoMirrored.Rounded.ArrowBack,
                navigationIconContentDescription = stringResource(Res.string.navigation_back),
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
        text = stringResource(Res.string.game_not_active),
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
        GameBoard(
            state = state,
            onCellClick = onCellClick,
        )
        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            text = stringResource(Res.string.reset_game),
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
            AppQueen(
                modifier = Modifier.size(width = 20.dp, height = 28.dp),
            )
        }
    }
}

private const val TIME_SEPARATOR = ':'

package com.alan.queensland.leaderboard.impl.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alan.queensland.core.ui.base.compose.components.AppAlertDialog
import com.alan.queensland.core.ui.base.compose.components.AppChessBoard
import com.alan.queensland.core.ui.base.compose.components.AppQueen
import com.alan.queensland.core.ui.base.compose.components.AppToolbar
import com.alan.queensland.core.ui.base.compose.themes.Paddings
import com.alan.queensland.core.ui.base.model.UiState
import org.jetbrains.compose.resources.stringResource
import queensland.leaderboard_impl.generated.resources.Res
import queensland.leaderboard_impl.generated.resources.delete_result
import queensland.leaderboard_impl.generated.resources.delete_result_confirmation_action
import queensland.leaderboard_impl.generated.resources.delete_result_confirmation_message
import queensland.leaderboard_impl.generated.resources.delete_result_confirmation_title
import queensland.leaderboard_impl.generated.resources.delete_result_failure_message
import queensland.leaderboard_impl.generated.resources.delete_result_failure_retry
import queensland.leaderboard_impl.generated.resources.delete_result_failure_title
import queensland.leaderboard_impl.generated.resources.dialog_cancel
import queensland.leaderboard_impl.generated.resources.leaderboard_board_size
import queensland.leaderboard_impl.generated.resources.leaderboard_completed_at
import queensland.leaderboard_impl.generated.resources.leaderboard_empty
import queensland.leaderboard_impl.generated.resources.leaderboard_load_error
import queensland.leaderboard_impl.generated.resources.leaderboard_solved_in
import queensland.leaderboard_impl.generated.resources.leaderboard_title
import queensland.leaderboard_impl.generated.resources.navigation_close

@Composable
fun LeaderBoardScreen(
    viewModel: LeaderBoardViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showDeletionFailureDialog by viewModel.showDeletionFailureDialogFlow.collectAsStateWithLifecycle()
    var resultUuidPendingDeletion by remember(viewModel) { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.leaderboard_title),
                navigationIcon = Icons.Default.Close,
                navigationIconContentDescription = stringResource(Res.string.navigation_close),
                onNavigationClick = viewModel::onBackClick,
            )
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = Paddings.one),
        ) {
            LeaderBoardContent(
                uiState = uiState,
                onDeleteResultClick = { uuid -> resultUuidPendingDeletion = uuid },
            )
        }
    }

    resultUuidPendingDeletion?.let { uuid ->
        AppAlertDialog(
            title = stringResource(Res.string.delete_result_confirmation_title),
            message = stringResource(Res.string.delete_result_confirmation_message),
            confirmButtonText = stringResource(Res.string.delete_result_confirmation_action),
            dismissButtonText = stringResource(Res.string.dialog_cancel),
            confirmButtonColor = MaterialTheme.colorScheme.error,
            onConfirmClick = {
                resultUuidPendingDeletion = null
                viewModel.onDeleteResultClick(uuid)
            },
            onDismissRequest = { resultUuidPendingDeletion = null },
        )
    }

    if (showDeletionFailureDialog) {
        AppAlertDialog(
            title = stringResource(Res.string.delete_result_failure_title),
            message = stringResource(Res.string.delete_result_failure_message),
            confirmButtonText = stringResource(Res.string.delete_result_failure_retry),
            dismissButtonText = stringResource(Res.string.dialog_cancel),
            onConfirmClick = viewModel::onDeleteResultRetryClick,
            onDismissClick = viewModel::onDeleteResultFailureDismissClick,
            onDismissRequest = viewModel::onDeleteResultFailureDismissClick,
        )
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
            text = stringResource(Res.string.leaderboard_load_error),
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
    if (state.groups.isEmpty()) {
        Text(
            text = stringResource(Res.string.leaderboard_empty),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge,
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = Paddings.one),
        verticalArrangement = Arrangement.spacedBy(Paddings.half),
    ) {
        state.groups.forEachIndexed { groupIndex, group ->
            item(key = "board-${group.boardSize}") {
                LeaderBoardGroupHeader(
                    boardSize = group.boardSize,
                    modifier = Modifier.padding(
                        top = if (groupIndex == 0) Paddings.half else Paddings.one,
                    ),
                )
            }
            items(
                items = group.results,
                key = GameResultUiState::uuid,
            ) { result ->
                GameResultRow(
                    boardSize = group.boardSize,
                    result = result,
                    onDeleteClick = { onDeleteResultClick(result.uuid) },
                )
            }
        }
    }
}

@Composable
private fun LeaderBoardGroupHeader(
    boardSize: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(
            Res.string.leaderboard_board_size,
            boardSize,
            boardSize,
        ),
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
private fun GameResultRow(
    boardSize: Int,
    result: GameResultUiState,
    onDeleteClick: () -> Unit,
) {
    val accentColor = rankAccentColor(result.rank)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.Top,
        ) {
            AppChessBoard(
                boardSize = boardSize,
                modifier = Modifier.size(128.dp),
                lightSquareColor = accentColor.copy(alpha = 0.18f),
                darkSquareColor = accentColor.copy(alpha = 0.72f),
                borderColor = accentColor,
            ) { row, column ->
                if ((row to column) in result.queenPositions) {
                    AppQueen(
                        modifier = Modifier.matchParentSize(),
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RankBadge(
                        rank = result.rank,
                        accentColor = accentColor,
                    )
                    FilledTonalIconButton(
                        onClick = onDeleteClick,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(Res.string.delete_result),
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(verticalArrangement = Arrangement.spacedBy(Paddings.half)) {
                    ResultMetadataRow(
                        text = stringResource(
                            Res.string.leaderboard_solved_in,
                            result.formattedTimeSpent,
                        ),
                        icon = Icons.Default.CheckCircle,
                        color = accentColor,
                    )
                    ResultMetadataRow(
                        text = stringResource(
                            Res.string.leaderboard_completed_at,
                            result.formattedCompletedAt,
                        ),
                        icon = Icons.Default.DateRange,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun RankBadge(
    rank: Int,
    accentColor: Color,
) {
    Surface(
        modifier = Modifier.size(32.dp),
        shape = CircleShape,
        color = accentColor.copy(alpha = 0.14f),
        contentColor = accentColor,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = rank.toString(),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun ResultMetadataRow(
    text: String,
    icon: ImageVector,
    color: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Paddings.half),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = color,
        )
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = color,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun rankAccentColor(rank: Int): Color = when (rank) {
    1 -> MaterialTheme.colorScheme.secondary
    2 -> MaterialTheme.colorScheme.onSurfaceVariant
    3 -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.primary
}

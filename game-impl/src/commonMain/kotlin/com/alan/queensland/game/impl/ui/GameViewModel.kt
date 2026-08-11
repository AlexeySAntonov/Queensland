package com.alan.queensland.game.impl.ui

import androidx.lifecycle.viewModelScope
import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.impl.business.ObserveActiveGameStateUseCase
import com.alan.queensland.game.impl.business.ToggleQueenUseCase
import com.alan.queensland.game.impl.business.ValidateQueenPlacementUseCase
import com.alan.queensland.game.impl.di.GameComponentHolder
import com.alan.queensland.navigation.api.Router
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

@Inject
class GameViewModel(
    observeActiveGameStateUseCase: ObserveActiveGameStateUseCase,
    validateQueenPlacementUseCase: ValidateQueenPlacementUseCase,
    coroutineDispatchers: CoroutineDispatchers,
    private val toggleQueenUseCase: ToggleQueenUseCase,
    private val router: Router,
) : BaseViewModel() {

    // TODO common UiState [Loading, Error, Data]
    val uiState: StateFlow<GameUiState?> = observeActiveGameStateUseCase()
        .map { activeGameState ->
            activeGameState?.let { state ->
                val validation = validateQueenPlacementUseCase(state)
                GameUiState(
                    boardSize = state.boardSize,
                    queenPositions = state.queenPositions,
                    conflictingPositions = validation.conflictingPositions,
                    remainingQueenCount = state.boardSize - state.queenPositions.size,
                )
            }
        }
        .flowOn(coroutineDispatchers.Processor)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = null,
        )

    override fun onCleared() {
        GameComponentHolder.reset()
        super.onCleared()
    }

    fun onBackClick() {
        router.back()
    }

    fun onCellClick(row: Int, column: Int) {
        toggleQueenUseCase(BoardPosition(row = row, column = column))
    }
}

package com.alan.queensland.game.impl.ui.configuration

import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.game.api.GameBoardSize
import com.alan.queensland.game.api.PieceType
import com.alan.queensland.game.impl.business.CreateGameUseCase
import com.alan.queensland.navigation.api.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject

@Inject
class GameConfigurationViewModel(
    private val createGameUseCase: CreateGameUseCase,
    private val router: Router,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(GameConfigurationUiState())
    val uiState: StateFlow<GameConfigurationUiState> = _uiState.asStateFlow()

    fun onDecreaseBoardSizeClick() {
        _uiState.update { current ->
            current.copy(
                boardSize = (current.boardSize - 1).coerceAtLeast(GameBoardSize.MIN),
            )
        }
    }

    fun onIncreaseBoardSizeClick() {
        _uiState.update { current ->
            current.copy(
                boardSize = (current.boardSize + 1).coerceAtMost(GameBoardSize.MAX),
            )
        }
    }

    fun onPieceTypeClick(pieceType: PieceType) {
        _uiState.update { current -> current.copy(selectedPieceType = pieceType) }
    }

    fun onContinueClick() {
        val state = _uiState.value
        createGameUseCase(
            boardSize = state.boardSize,
            pieceType = state.selectedPieceType,
        )
    }

    fun onBackClick() {
        router.back()
    }
}

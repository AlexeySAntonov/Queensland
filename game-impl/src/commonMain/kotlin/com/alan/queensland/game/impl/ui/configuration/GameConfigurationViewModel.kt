package com.alan.queensland.game.impl.ui.configuration

import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.game.api.CreateGameUseCase
import com.alan.queensland.core.ui.base.util.GameBoardSize
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

    private val _boardSize = MutableStateFlow(GameBoardSize.DEFAULT)
    val boardSize: StateFlow<Int> = _boardSize.asStateFlow()

    fun onDecreaseBoardSizeClick() {
        _boardSize.update { current ->
            (current - 1).coerceAtLeast(GameBoardSize.MIN)
        }
    }

    fun onIncreaseBoardSizeClick() {
        _boardSize.update { current ->
            (current + 1).coerceAtMost(GameBoardSize.MAX)
        }
    }

    fun onContinueClick() {
        createGameUseCase(_boardSize.value)
    }

    fun onBackClick() {
        router.back()
    }
}

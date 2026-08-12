package com.alan.queensland.game.impl.ui

import androidx.lifecycle.viewModelScope
import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.core.ui.base.model.UiState
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.impl.business.AddElapsedGameTimeUseCase
import com.alan.queensland.game.impl.business.ObserveActiveGameStateUseCase
import com.alan.queensland.game.impl.business.ResetGameUseCase
import com.alan.queensland.game.impl.business.ToggleQueenUseCase
import com.alan.queensland.game.impl.business.ValidateQueenPlacementUseCase
import com.alan.queensland.game.impl.di.GameComponentHolder
import com.alan.queensland.navigation.api.Router
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Inject
class GameViewModel(
    observeActiveGameStateUseCase: ObserveActiveGameStateUseCase,
    validateQueenPlacementUseCase: ValidateQueenPlacementUseCase,
    coroutineDispatchers: CoroutineDispatchers,
    gameUiStateMapper: GameUiStateMapper,
    private val addElapsedGameTimeUseCase: AddElapsedGameTimeUseCase,
    private val resetGameUseCase: ResetGameUseCase,
    private val toggleQueenUseCase: ToggleQueenUseCase,
    private val router: Router,
) : BaseViewModel() {

    private var timerJob: Job? = null
    private var timerStartedAt: TimeMark? = null
    private val currentSessionElapsedMillis = MutableStateFlow(0L)

    val uiState = observeActiveGameStateUseCase()
        .filterNotNull()
        .map { state -> state to validateQueenPlacementUseCase(state) }
        .flowOn(coroutineDispatchers.Processor)
        .combine(currentSessionElapsedMillis) { (state, validation), currentSessionElapsedMillis ->
            UiState.Data(
                value = gameUiStateMapper(
                    state = state,
                    validation = validation,
                    currentSessionElapsedMillis = currentSessionElapsedMillis,
                ),
            )
        }
        .catch<UiState<GameUiState>> { emit(UiState.Error) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = UI_STATE_SHARING_TIMEOUT_MILLIS),
            initialValue = UiState.Loading,
        )

    override fun onCleared() {
        stopTimer()
        GameComponentHolder.reset()
        super.onCleared()
    }

    fun onScreenResumed() {
        resumeTimer()
    }

    fun onScreenPaused() {
        stopTimer()
    }

    fun onBackClick() {
        router.back()
    }

    fun onCellClick(row: Int, column: Int) {
        toggleQueenUseCase(BoardPosition(row = row, column = column))
    }

    fun onResetGameClick() {
        timerStartedAt = TimeSource.Monotonic.markNow()
        currentSessionElapsedMillis.value = 0L
        resetGameUseCase()
    }

    private fun resumeTimer() {
        if (timerJob?.isActive == true) return

        timerStartedAt = TimeSource.Monotonic.markNow()
        currentSessionElapsedMillis.value = 0L
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(TIMER_UPDATE_INTERVAL_MILLIS.milliseconds)
                currentSessionElapsedMillis.value = timerStartedAt?.elapsedNow()?.inWholeMilliseconds ?: 0L
            }
        }
    }

    private fun stopTimer() {
        val elapsedMillis = timerStartedAt?.elapsedNow()?.inWholeMilliseconds

        timerJob?.cancel()
        timerJob = null
        timerStartedAt = null
        currentSessionElapsedMillis.value = 0L
        elapsedMillis?.let(addElapsedGameTimeUseCase::invoke)
    }

    private companion object {
        const val TIMER_UPDATE_INTERVAL_MILLIS = 500L
        const val UI_STATE_SHARING_TIMEOUT_MILLIS = 5_000L
    }
}

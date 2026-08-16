package com.alan.queensland.game.impl.ui

import androidx.lifecycle.viewModelScope
import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.core.ui.base.model.UiState
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.impl.business.AbandonGameUseCase
import com.alan.queensland.game.impl.business.AddElapsedGameTimeUseCase
import com.alan.queensland.game.impl.business.CompleteGameUseCase
import com.alan.queensland.game.impl.business.ObserveActiveGameStateUseCase
import com.alan.queensland.game.impl.business.ResetGameUseCase
import com.alan.queensland.game.impl.business.ToggleQueenUseCase
import com.alan.queensland.game.impl.business.ValidateQueenPlacementUseCase
import com.alan.queensland.navigation.api.Router
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Inject
class GameViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    gameUiStateMapper: GameUiStateMapper,
    validateQueenPlacementUseCase: ValidateQueenPlacementUseCase,
    private val observeActiveGameStateUseCase: ObserveActiveGameStateUseCase,
    private val abandonGameUseCase: AbandonGameUseCase,
    private val addElapsedGameTimeUseCase: AddElapsedGameTimeUseCase,
    private val completeGameUseCase: CompleteGameUseCase,
    private val resetGameUseCase: ResetGameUseCase,
    private val toggleQueenUseCase: ToggleQueenUseCase,
    private val router: Router,
) : BaseViewModel() {

    private var timerJob: Job? = null
    private var timerStartedAt: TimeMark? = null
    private val currentSessionElapsedMillis = MutableStateFlow(0L)

    private val _showCompletionFailureDialogFlow = MutableStateFlow(false)
    val showCompletionFailureDialogFlow = _showCompletionFailureDialogFlow.asStateFlow()

    private val validatedGameState = observeActiveGameStateUseCase()
        .filterNotNull()
        .map { state -> state to validateQueenPlacementUseCase(state) }
        .flowOn(coroutineDispatchers.Processor)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1,
        )

    val uiState = validatedGameState
        .combine(currentSessionElapsedMillis) { (state, validation), currentSessionElapsedMillis ->
            gameUiStateMapper(
                state = state,
                validation = validation,
                currentSessionElapsedMillis = currentSessionElapsedMillis,
            )
        }
        .flowOn(coroutineDispatchers.Processor)
        .map<GameUiState, UiState<GameUiState>> { gameUiState -> UiState.Data(gameUiState) }
        .catch { emit(UiState.Error) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = UI_STATE_SHARING_TIMEOUT_MILLIS),
            initialValue = UiState.Loading,
        )

    init {
        observeGameTermination()
    }

    override fun onCleared() {
        stopTimer()
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

    fun onGameCompletionRetryClick() {
        _showCompletionFailureDialogFlow.value = false
        viewModelScope.launch {
            completeGame()
        }
    }

    fun onAbandonGameClick() {
        _showCompletionFailureDialogFlow.value = false
        abandonGameUseCase()
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

    private fun observeGameTermination() {
        viewModelScope.launch {
            // early return when, e.g., the game was torn down by process death and we no longer able to restore it
            if (observeActiveGameStateUseCase().first() == null) {
                router.back()
                return@launch
            }

            validatedGameState.first { (_, validation) ->
                validation.isSolved
            }

            // to exclude QUEEN_LANDING_DURATION_MILLIS from result time spent
            stopTimer()

            // wait until the last queen completes its landing
            delay(QUEEN_LANDING_DURATION_MILLIS.milliseconds)
            completeGame()
        }
    }

    private suspend fun completeGame() {
        completeGameUseCase().onFailure {
            _showCompletionFailureDialogFlow.value = true
        }
    }

    private companion object {
        const val TIMER_UPDATE_INTERVAL_MILLIS = 500L
        const val UI_STATE_SHARING_TIMEOUT_MILLIS = 5_000L
    }
}

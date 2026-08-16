package com.alan.queensland.game.impl.ui

import androidx.lifecycle.viewModelScope
import com.alan.queensland.core.db.test.FakeGameResultsDatasource
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import com.alan.queensland.game.impl.business.AbandonGameUseCase
import com.alan.queensland.game.impl.business.AddElapsedGameTimeUseCase
import com.alan.queensland.game.impl.business.CompleteGameUseCase
import com.alan.queensland.game.impl.business.ObserveActiveGameStateUseCase
import com.alan.queensland.game.impl.business.ResetGameUseCase
import com.alan.queensland.game.impl.business.ToggleQueenUseCase
import com.alan.queensland.game.impl.business.ValidateQueenPlacementUseCase
import com.alan.queensland.game.impl.data.GameRepositoryImpl
import com.alan.queensland.navigation.api.NavigationEvent
import com.alan.queensland.navigation.test.FakeRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    @Test
    fun failedGameCompletionCanBeRetried() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        var viewModel: GameViewModel? = null

        try {
            val failure = IllegalStateException("Database unavailable")
            val datasource = FakeGameResultsDatasource().apply {
                saveResultFailure = failure
            }
            val repository = GameRepositoryImpl(datasource).apply {
                updateActiveGameState { solvedGameState() }
            }
            val router = FakeRouter()
            val testedViewModel = GameViewModel(
                coroutineDispatchers = CoroutineDispatchers(
                    dispatcherMain = dispatcher,
                    dispatcherIo = dispatcher,
                    dispatcherDefault = dispatcher,
                ),
                gameUiStateMapper = GameUiStateMapper(),
                validateQueenPlacementUseCase = ValidateQueenPlacementUseCase(),
                observeActiveGameStateUseCase = ObserveActiveGameStateUseCase(repository),
                abandonGameUseCase = AbandonGameUseCase(repository, router),
                addElapsedGameTimeUseCase = AddElapsedGameTimeUseCase(repository),
                completeGameUseCase = CompleteGameUseCase(repository, router),
                resetGameUseCase = ResetGameUseCase(repository),
                toggleQueenUseCase = ToggleQueenUseCase(repository),
                router = router,
            )
            viewModel = testedViewModel

            advanceUntilIdle()

            assertEquals(true, testedViewModel.showCompletionFailureDialogFlow.value)
            assertEquals(emptyList<NavigationEvent>(), router.sentEvents)
            assertEquals(solvedGameState(), repository.observeActiveGameState().value)

            datasource.saveResultFailure = null
            testedViewModel.onGameCompletionRetryClick()
            advanceUntilIdle()

            assertEquals(false, testedViewModel.showCompletionFailureDialogFlow.value)
            assertEquals(1, datasource.savedResults.size)
            assertNull(repository.observeActiveGameState().value)
            assertEquals(
                listOf<NavigationEvent>(NavigationEvent.OpenGameFinished),
                router.sentEvents,
            )
        } finally {
            viewModel?.viewModelScope?.cancel()
            Dispatchers.resetMain()
        }
    }

    private fun solvedGameState() = ActiveGameState(
        boardSize = 4,
        queenPositions = setOf(
            BoardPosition(row = 0, column = 1),
            BoardPosition(row = 1, column = 3),
            BoardPosition(row = 2, column = 0),
            BoardPosition(row = 3, column = 2),
        ),
        timeSpentMillis = 98_765L,
    )
}

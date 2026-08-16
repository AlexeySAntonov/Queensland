package com.alan.queensland.leaderboard.impl.ui

import androidx.lifecycle.viewModelScope
import com.alan.queensland.core.db.test.FakeGameResultsDatasource
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import com.alan.queensland.leaderboard.impl.business.DeleteResultUseCase
import com.alan.queensland.leaderboard.impl.business.ObserveLeaderBoardUseCase
import com.alan.queensland.leaderboard.impl.data.LeaderBoardRepository
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

@OptIn(ExperimentalCoroutinesApi::class)
class LeaderBoardViewModelTest {

    @Test
    fun failedResultDeletionCanBeRetried() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        var viewModel: LeaderBoardViewModel? = null

        try {
            val datasource = FakeGameResultsDatasource().apply {
                deleteResultFailure = IllegalStateException("Database unavailable")
            }
            val repository = LeaderBoardRepository(datasource)
            val testedViewModel = LeaderBoardViewModel(
                observeLeaderBoardUseCase = ObserveLeaderBoardUseCase(repository),
                uiStateMapper = LeaderBoardUiStateMapper(),
                coroutineDispatchers = CoroutineDispatchers(
                    dispatcherMain = dispatcher,
                    dispatcherIo = dispatcher,
                    dispatcherDefault = dispatcher,
                ),
                deleteResultUseCase = DeleteResultUseCase(repository),
                router = FakeRouter(),
            )
            viewModel = testedViewModel

            testedViewModel.onDeleteResultClick(RESULT_UUID)
            advanceUntilIdle()

            assertEquals(true, testedViewModel.showDeletionFailureDialogFlow.value)
            assertEquals(emptyList(), datasource.deletedResultUuids)

            datasource.deleteResultFailure = null
            testedViewModel.onDeleteResultRetryClick()
            advanceUntilIdle()

            assertEquals(false, testedViewModel.showDeletionFailureDialogFlow.value)
            assertEquals(listOf(RESULT_UUID), datasource.deletedResultUuids)
        } finally {
            viewModel?.viewModelScope?.cancel()
            Dispatchers.resetMain()
        }
    }

    private companion object {
        const val RESULT_UUID = "result-uuid"
    }
}

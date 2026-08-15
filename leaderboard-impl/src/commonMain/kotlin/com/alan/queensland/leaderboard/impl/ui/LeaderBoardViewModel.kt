package com.alan.queensland.leaderboard.impl.ui

import androidx.lifecycle.viewModelScope
import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.core.ui.base.model.UiState
import com.alan.queensland.core.utils.flow.CoroutineDispatchers
import com.alan.queensland.leaderboard.impl.business.DeleteResultUseCase
import com.alan.queensland.leaderboard.impl.business.ObserveLeaderBoardUseCase
import com.alan.queensland.leaderboard.impl.di.LeaderBoardComponentHolder
import com.alan.queensland.navigation.api.Router
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class LeaderBoardViewModel(
    observeLeaderBoardUseCase: ObserveLeaderBoardUseCase,
    uiStateMapper: LeaderBoardUiStateMapper,
    coroutineDispatchers: CoroutineDispatchers,
    private val deleteResultUseCase: DeleteResultUseCase,
    private val router: Router,
) : BaseViewModel() {

    val uiState = observeLeaderBoardUseCase()
        .map { results -> UiState.Data(uiStateMapper(results)) }
        .flowOn(coroutineDispatchers.Processor)
        .catch<UiState<LeaderBoardUiState>> { emit(UiState.Error) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = UI_STATE_SHARING_TIMEOUT_MILLIS),
            initialValue = UiState.Loading,
        )

    override fun onCleared() {
        LeaderBoardComponentHolder.reset()
        super.onCleared()
    }

    fun onBackClick() {
        router.back()
    }

    fun onDeleteResultClick(uuid: String) {
        viewModelScope.launch {
            deleteResultUseCase(uuid)
        }
    }

    private companion object {
        const val UI_STATE_SHARING_TIMEOUT_MILLIS = 5_000L
    }
}

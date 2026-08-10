package com.alan.queensland.home.impl.ui

import androidx.lifecycle.viewModelScope
import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.game.api.ObserveHasActiveGameUseCase
import com.alan.queensland.game.api.ResumeGameUseCase
import com.alan.queensland.game.api.StartNewGameUseCase
import com.alan.queensland.home.impl.di.HomeComponentHolder
import com.alan.queensland.navigation.api.Router
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

@Inject
class HomeViewModel(
    observeHasActiveGameUseCase: ObserveHasActiveGameUseCase,
    private val startNewGameUseCase: StartNewGameUseCase,
    private val resumeGameUseCase: ResumeGameUseCase,
    private val router: Router,
) : BaseViewModel() {

    val hasActiveGame: StateFlow<Boolean> = observeHasActiveGameUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = false,
        )

    override fun onCleared() {
        HomeComponentHolder.reset()
        super.onCleared()
    }

    fun onNewGameClick() {
        startNewGameUseCase()
    }

    fun onResumeGameClick() {
        resumeGameUseCase()
    }

    fun onSeeResultsClick() {
        router.openResults()
    }
}

package com.alan.queensland.game.impl.ui

import androidx.lifecycle.viewModelScope
import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.ObserveActiveGameStateUseCase
import com.alan.queensland.game.impl.di.GameComponentHolder
import com.alan.queensland.navigation.api.Router
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

@Inject
class GameViewModel(
    observeActiveGameStateUseCase: ObserveActiveGameStateUseCase,
    private val router: Router,
) : BaseViewModel() {

    val activeGameState: StateFlow<ActiveGameState?> = observeActiveGameStateUseCase()
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
}

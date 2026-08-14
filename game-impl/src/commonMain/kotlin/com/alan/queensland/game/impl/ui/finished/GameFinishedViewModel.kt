package com.alan.queensland.game.impl.ui.finished

import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.game.impl.di.GameComponentHolder
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Inject

@Inject
class GameFinishedViewModel(
    private val router: Router,
) : BaseViewModel() {

    override fun onCleared() {
        GameComponentHolder.reset()
        super.onCleared()
    }

    fun onCloseClick() {
        router.back()
    }
}

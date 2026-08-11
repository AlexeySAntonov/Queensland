package com.alan.queensland.game.impl.ui.results

import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Inject

// TODO extract into stadnalone leaderboard module w own component, etc
@Inject
class ResultsViewModel(
    private val router: Router,
) : BaseViewModel() {

    fun onBackClick() {
        router.back()
    }
}

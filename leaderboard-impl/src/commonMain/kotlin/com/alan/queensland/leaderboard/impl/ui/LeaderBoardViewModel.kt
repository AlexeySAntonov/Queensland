package com.alan.queensland.leaderboard.impl.ui

import com.alan.queensland.core.ui.base.lifecycle.BaseViewModel
import com.alan.queensland.leaderboard.impl.di.LeaderBoardComponentHolder
import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Inject

@Inject
class LeaderBoardViewModel(
    private val router: Router,
) : BaseViewModel() {

    override fun onCleared() {
        LeaderBoardComponentHolder.reset()
        super.onCleared()
    }

    fun onBackClick() {
        router.back()
    }
}

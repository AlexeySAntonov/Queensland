package com.alan.queensland.leaderboard.api

import com.alan.queensland.navigation.api.Router
import me.tatarka.inject.annotations.Inject

@Inject
class OpenLeaderBoardUseCase(
    private val router: Router,
) {
    operator fun invoke() {
        router.openLeaderBoard()
    }
}

package com.alan.queensland.game.impl.ui.configuration

import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.GameBoardSize
import com.alan.queensland.game.api.PieceType
import com.alan.queensland.game.impl.business.CreateGameUseCase
import com.alan.queensland.game.test.FakeGameRepository
import com.alan.queensland.navigation.api.NavigationEvent
import com.alan.queensland.navigation.test.FakeRouter
import kotlin.test.Test
import kotlin.test.assertEquals

class GameConfigurationViewModelTest {

    @Test
    fun selectedPieceTypeIsPreservedAndUsedToCreateGame() {
        val repository = FakeGameRepository()
        val router = FakeRouter()
        val viewModel = GameConfigurationViewModel(
            createGameUseCase = CreateGameUseCase(repository, router),
            router = router,
        )

        viewModel.onPieceTypeClick(PieceType.ROOK)
        viewModel.onIncreaseBoardSizeClick()

        assertEquals(PieceType.ROOK, viewModel.uiState.value.selectedPieceType)

        viewModel.onContinueClick()

        assertEquals(
            ActiveGameState(
                boardSize = GameBoardSize.DEFAULT + 1,
                pieceType = PieceType.ROOK,
            ),
            repository.currentState,
        )
        assertEquals(listOf(NavigationEvent.OpenGame), router.sentEvents)
    }
}

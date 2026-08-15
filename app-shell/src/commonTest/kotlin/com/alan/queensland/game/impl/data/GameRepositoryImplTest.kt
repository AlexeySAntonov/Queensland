package com.alan.queensland.game.impl.data

import com.alan.queensland.core.db.test.FakeGameResultsDatasource
import com.alan.queensland.core.db.test.SavedGameResult
import com.alan.queensland.game.api.ActiveGameState
import com.alan.queensland.game.api.BoardPosition
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GameRepositoryImplTest {

    @Test
    fun completingActiveGameSavesResultAndClearsCache() = runTest {
        val datasource = FakeGameResultsDatasource()
        val repository = GameRepositoryImpl(datasource)
        val activeGame = ActiveGameState(
            boardSize = 4,
            queenPositions = setOf(
                BoardPosition(row = 0, column = 1),
                BoardPosition(row = 1, column = 3),
                BoardPosition(row = 2, column = 0),
                BoardPosition(row = 3, column = 2),
            ),
            timeSpentMillis = 98_765L,
        )
        repository.updateActiveGameState { activeGame }

        assertTrue(repository.completeActiveGame())

        assertEquals(
            listOf(
                SavedGameResult(
                    boardSize = 4,
                    timeSpentMillis = 98_765L,
                    queenPositions = setOf(
                        0 to 1,
                        1 to 3,
                        2 to 0,
                        3 to 2,
                    ),
                ),
            ),
            datasource.savedResults,
        )
        assertNull(repository.observeActiveGameState().first())
    }

    @Test
    fun completingWithoutActiveGameDoesNotSaveResult() = runTest {
        val datasource = FakeGameResultsDatasource()
        val repository = GameRepositoryImpl(datasource)

        assertFalse(repository.completeActiveGame())

        assertEquals(emptyList<SavedGameResult>(), datasource.savedResults)
    }
}

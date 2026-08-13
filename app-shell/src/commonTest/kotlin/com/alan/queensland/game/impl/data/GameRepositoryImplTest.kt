package com.alan.queensland.game.impl.data

import com.alan.queensland.core.db.test.FakeGameResultsDatasource
import com.alan.queensland.game.api.ActiveGameState
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
            boardSize = 8,
            timeSpentMillis = 98_765L,
        )
        repository.updateActiveGameState { activeGame }

        assertTrue(repository.completeActiveGame())

        assertEquals(
            listOf(FakeGameResultsDatasource.SavedResult(boardSize = 8, timeSpentMillis = 98_765L)),
            datasource.savedResults,
        )
        assertNull(repository.observeActiveGameState().first())
    }

    @Test
    fun completingWithoutActiveGameDoesNotSaveResult() = runTest {
        val datasource = FakeGameResultsDatasource()
        val repository = GameRepositoryImpl(datasource)

        assertFalse(repository.completeActiveGame())

        assertEquals(emptyList<FakeGameResultsDatasource.SavedResult>(), datasource.savedResults)
    }
}

package com.alan.queensland.core.utils.flow

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler

class CoroutineDispatchers(
    dispatcherMain: CoroutineDispatcher,
    dispatcherIo: CoroutineDispatcher,
    dispatcherDefault: CoroutineDispatcher,
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable !is CancellationException) {
            throwable.printStackTrace() // TODO: add logger
        }
    }

    val Main = dispatcherMain
    val IO = dispatcherIo + exceptionHandler
    val Processor = dispatcherDefault + exceptionHandler
}

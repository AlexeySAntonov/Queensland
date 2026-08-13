package com.alan.queensland.core.utils.time

fun formatElapsedTime(timeSpentMillis: Long): String {
    val totalSeconds = timeSpentMillis.coerceAtLeast(0L) / MILLIS_PER_SECOND
    val hours = totalSeconds / SECONDS_PER_HOUR
    val minutes = (totalSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
    val seconds = totalSeconds % SECONDS_PER_MINUTE

    return if (hours > 0L) {
        "$hours:${minutes.toTwoDigits()}:${seconds.toTwoDigits()}"
    } else {
        "${minutes.toTwoDigits()}:${seconds.toTwoDigits()}"
    }
}

private fun Long.toTwoDigits() = toString().padStart(length = 2, padChar = '0')

private const val MILLIS_PER_SECOND = 1_000L
private const val SECONDS_PER_MINUTE = 60L
private const val SECONDS_PER_HOUR = 3_600L

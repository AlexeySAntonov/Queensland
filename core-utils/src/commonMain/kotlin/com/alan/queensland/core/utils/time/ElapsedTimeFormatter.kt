package com.alan.queensland.core.utils.time

fun formatElapsedTime(
    timeSpentMillis: Long,
    includeMilliseconds: Boolean = false,
): String {
    val normalizedTimeSpentMillis = timeSpentMillis.coerceAtLeast(0L)
    val totalSeconds = normalizedTimeSpentMillis / MILLIS_PER_SECOND
    val hours = totalSeconds / SECONDS_PER_HOUR
    val minutes = (totalSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
    val seconds = totalSeconds % SECONDS_PER_MINUTE

    val formattedTime = if (hours > 0L) {
        "$hours:${minutes.toTwoDigits()}:${seconds.toTwoDigits()}"
    } else {
        "${minutes.toTwoDigits()}:${seconds.toTwoDigits()}"
    }

    return if (includeMilliseconds) {
        val milliseconds = normalizedTimeSpentMillis % MILLIS_PER_SECOND
        "$formattedTime.${milliseconds.toThreeDigits()}"
    } else {
        formattedTime
    }
}

private fun Long.toTwoDigits() = toString().padStart(length = 2, padChar = '0')
private fun Long.toThreeDigits() = toString().padStart(length = 3, padChar = '0')

private const val MILLIS_PER_SECOND = 1_000L
private const val SECONDS_PER_MINUTE = 60L
private const val SECONDS_PER_HOUR = 3_600L

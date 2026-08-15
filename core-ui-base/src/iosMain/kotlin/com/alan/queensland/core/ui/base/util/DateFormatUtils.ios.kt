package com.alan.queensland.core.ui.base.util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterFullStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970

actual fun formatFullLocalDateTime(epochMillis: Long): String {
    val formatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterFullStyle
        timeStyle = NSDateFormatterShortStyle
        locale = NSLocale.currentLocale
    }
    val date = NSDate.dateWithTimeIntervalSince1970(
        epochMillis / MILLIS_PER_SECOND,
    )
    return formatter.stringFromDate(date)
}

private const val MILLIS_PER_SECOND = 1_000.0

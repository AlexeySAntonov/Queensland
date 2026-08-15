package com.alan.queensland.core.ui.base.util

import java.text.DateFormat
import java.util.Date

actual fun formatFullLocalDateTime(epochMillis: Long): String =
    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT)
        .format(Date(epochMillis))

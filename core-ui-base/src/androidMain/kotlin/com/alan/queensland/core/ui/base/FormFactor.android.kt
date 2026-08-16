package com.alan.queensland.core.ui.base

import com.alan.queensland.core.utils.context.AppContextProvider

actual object FormFactor {
    actual fun isTablet(): Boolean =
        AppContextProvider.get().resources.getBoolean(R.bool.is_tablet)
}

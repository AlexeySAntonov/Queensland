package com.alan.queensland.core.ui.base

import platform.UIKit.UIDevice
import platform.UIKit.UIUserInterfaceIdiomPad

actual object FormFactor {
    actual fun isTablet(): Boolean =
        UIDevice.currentDevice.userInterfaceIdiom == UIUserInterfaceIdiomPad
}

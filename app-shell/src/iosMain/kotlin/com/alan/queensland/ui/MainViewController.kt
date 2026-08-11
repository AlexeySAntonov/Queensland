package com.alan.queensland.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.alan.queensland.di.AppComponent

private val appComponent by lazy { AppComponent.init() }

fun MainViewController() = ComposeUIViewController {
    QueenslandApp(appComponent = appComponent)
}

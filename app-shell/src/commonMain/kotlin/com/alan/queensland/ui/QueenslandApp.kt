package com.alan.queensland.ui

import androidx.compose.runtime.Composable
import com.alan.queensland.core.ui.base.compose.components.AppBackground
import com.alan.queensland.core.ui.base.compose.themes.AppTheme
import com.alan.queensland.di.AppComponent
import com.alan.queensland.ui.navigation.AppNavGraph

@Composable
fun QueenslandApp(
    appComponent: AppComponent,
) {
    AppTheme {
        AppBackground {
            AppNavGraph(appComponent = appComponent)
        }
    }
}

package com.alan.queensland.core.ui.base.compose.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorScheme(
            primary = Color(0xFF9DD6C5),
            secondary = Color(0xFFF1C27D),
            tertiary = Color(0xFFAEC6F0),
            background = Color(0xFF111315),
            surface = Color(0xFF1B1F22),
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF1D6F5A),
            secondary = Color(0xFF9A5B18),
            tertiary = Color(0xFF315F9D),
            background = Color(0xFFFAFAF7),
            surface = Color.White,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}

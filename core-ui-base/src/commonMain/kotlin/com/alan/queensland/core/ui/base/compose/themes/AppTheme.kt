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
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}

private val DarkBrown = Color(0xFF352A22)
private val Ivory = Color(0xFFF4E8C8)

private val LightColorScheme = lightColorScheme(
    primary = DarkBrown,
    onPrimary = Ivory,
    primaryContainer = Color(0xFFF1E5D3),
    onPrimaryContainer = Color(0xFF2C241A),
    secondary = Color(0xFF557A73),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD8ECE6),
    onSecondaryContainer = Color(0xFF173B35),
    tertiary = Color(0xFF536B8F),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD9E3FA),
    onTertiaryContainer = Color(0xFF172A48),
    background = Color(0xFFFAF8F3),
    onBackground = Color(0xFF211D18),
    surface = Color(0xFFFFFBF5),
    onSurface = Color(0xFF211D18),
    surfaceVariant = Color(0xFFFFF7E5),
    onSurfaceVariant = Color(0xFF5F574B),
    surfaceContainerLow = Color(0xFFF4EFE5),
    outline = Color(0xFF807563),
    outlineVariant = Color(0xFFD2C7B4),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

private val DarkColorScheme = darkColorScheme(
    primary = Ivory,
    onPrimary = DarkBrown,
    primaryContainer = Color(0xFF514235),
    onPrimaryContainer = Color(0xFFF8ECD2),
    secondary = Color(0xFF9FCFC4),
    onSecondary = Color(0xFF07372F),
    secondaryContainer = Color(0xFF274E47),
    onSecondaryContainer = Color(0xFFBAEBDD),
    tertiary = Color(0xFFB7C9EC),
    onTertiary = Color(0xFF20304A),
    tertiaryContainer = Color(0xFF374763),
    onTertiaryContainer = Color(0xFFD9E2FF),
    background = Color(0xFF11100F),
    onBackground = Color(0xFFF2ECE4),
    surface = Color(0xFF1B1815),
    onSurface = Color(0xFFF2ECE4),
    surfaceVariant = Color(0xFF0D0A08),
    onSurfaceVariant = Color(0xFFCFC4B8),
    surfaceContainerLow = Color(0xFF201C19),
    outline = Color(0xFF9B8E80),
    outlineVariant = Color(0xFF4A4138),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

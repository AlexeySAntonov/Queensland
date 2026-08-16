package com.alan.queensland.core.ui.base.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.MaterialTheme
import com.alan.queensland.core.ui.base.FormFactor
import org.jetbrains.compose.resources.painterResource
import queensland.core_ui_base.generated.resources.Res
import queensland.core_ui_base.generated.resources.app_background
import queensland.core_ui_base.generated.resources.app_background_light
import queensland.core_ui_base.generated.resources.app_background_wide
import queensland.core_ui_base.generated.resources.app_background_wide_light

@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val isWideScreen = FormFactor.isWideScreen()
    val backgroundResource = when {
        isWideScreen && isDarkTheme -> Res.drawable.app_background_wide
        isWideScreen -> Res.drawable.app_background_wide_light
        isDarkTheme -> Res.drawable.app_background
        else -> Res.drawable.app_background_light
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(backgroundResource),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background.copy(
                        alpha = if (isDarkTheme) {
                            DARK_THEME_SCRIM_ALPHA
                        } else {
                            LIGHT_THEME_SCRIM_ALPHA
                        },
                    ),
                ),
        )
        content()
    }
}

private const val DARK_THEME_SCRIM_ALPHA = 0.28f
private const val LIGHT_THEME_SCRIM_ALPHA = 0f

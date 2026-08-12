package com.alan.queensland.core.ui.base.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary,
    isOutlined: Boolean = false,
) {
    val buttonModifier = modifier.requiredHeight(56.dp)
    val coroutineScope = rememberCoroutineScope()
    var acceptsClicks by remember { mutableStateOf(true) }
    val guardedOnClick: () -> Unit = {
        if (acceptsClicks) {
            acceptsClicks = false
            coroutineScope.launch {
                delay(CLICK_THROTTLE_MILLIS.milliseconds)
                acceptsClicks = true
            }
            onClick()
        }
    }

    if (isOutlined) {
        OutlinedButton(
            onClick = guardedOnClick,
            modifier = buttonModifier,
            enabled = enabled,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
            border = BorderStroke(
                width = 1.dp,
                color = if (enabled) color else MaterialTheme.colorScheme.outlineVariant,
            ),
        ) {
            AppButtonText(text)
        }
    } else {
        Button(
            onClick = guardedOnClick,
            modifier = buttonModifier,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = contentColorFor(color),
            ),
        ) {
            AppButtonText(text)
        }
    }
}

@Composable
private fun AppButtonText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
    )
}

private const val CLICK_THROTTLE_MILLIS = 300L

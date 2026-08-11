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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

    if (isOutlined) {
        OutlinedButton(
            onClick = onClick,
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
            onClick = onClick,
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

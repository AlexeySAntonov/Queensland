package com.alan.queensland.core.ui.base.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.alan.queensland.core.ui.base.compose.themes.Paddings

@Composable
fun AppAlertDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissButtonText: String? = null,
    onDismissClick: () -> Unit = onDismissRequest,
    confirmButtonColor: Color = MaterialTheme.colorScheme.primary,
    dismissButtonColor: Color = MaterialTheme.colorScheme.primary,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            Column(
                modifier = Modifier.padding(Paddings.one),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.error,
                )
                Spacer(modifier = Modifier.height(Paddings.half))
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(Paddings.half))
                Text(
                    text = message,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(Paddings.one))
                AppButton(
                    text = confirmButtonText,
                    onClick = onConfirmClick,
                    modifier = Modifier.fillMaxWidth(),
                    color = confirmButtonColor,
                )
                dismissButtonText?.let {
                    Spacer(modifier = Modifier.height(Paddings.half))
                    AppButton(
                        text = dismissButtonText,
                        onClick = onDismissClick,
                        modifier = Modifier.fillMaxWidth(),
                        color = dismissButtonColor,
                        isOutlined = true,
                    )
                }
            }
        }
    }
}

package com.example.myapplication.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun ConfirmationDialog(
    title: String? = null,
    message: String,
    action: String,
    dismissAction: String = stringResource(android.R.string.cancel),
    onConfirmed: (agreed: Boolean) -> Unit,
) {
    AlertDialog(
        title = {
            if (title != null) {
                Text(title)
            }
        },
        text = {
            Text(message)
        },
        onDismissRequest = {
            onConfirmed(false)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmed(true)
                }
            ) {
                Text(action)
            }
        },
        dismissButton = {
            TextButton(onClick = { onConfirmed(false) }) {
                Text(dismissAction)
            }
        }
    )
}
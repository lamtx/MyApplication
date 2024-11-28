package com.example.myapplication.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@Composable
fun ExceptionDialog(
    exception: Exception?,
    title: String,
    onDismiss: () -> Unit,
) {
    if (exception == null) {
        return
    }
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            Text(exception.toMessage(context))
        },
        confirmButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(text = stringResource(android.R.string.ok))
            }
        }
    )
}
package com.example.myapplication.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ExceptionInfo(
    exception: Exception,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Text(
        modifier = modifier,
        text = exception.toMessage(context),
        textAlign = TextAlign.Center,
    )
}
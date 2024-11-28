@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.ui.articledetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.ui.common.ConfirmationDialog
import com.example.myapplication.ui.common.ExceptionDialog
import com.example.myapplication.ui.common.ExceptionInfo
import com.example.myapplication.ui.common.formatDateOnly
import com.example.myapplication.ui.theme.horizontalSpacing
import com.example.myapplication.ui.theme.itemTopSpacing
import com.example.myapplication.ui.theme.wideItemTopSpacing

@Composable
fun ArticleDetail(
    state: ArticleDetailsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    LaunchedEffect(state.isFinishRequested) {
        if (state.isFinishRequested) {
            onBack()
        }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = {
                    Text(state.article?.title ?: stringResource(R.string.article_details))
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
                .horizontalSpacing()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            state.errorContent?.let { exception ->
                ExceptionInfo(
                    modifier = Modifier.horizontalSpacing(),
                    exception = exception,
                )
            }
            state.article?.let { item ->
                Text(
                    text = formatDateOnly(item.date),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    modifier = Modifier.itemTopSpacing(),
                    text = item.description,
                    style = MaterialTheme.typography.bodyLarge,
                )
                OutlinedButton(
                    modifier = Modifier.wideItemTopSpacing(),
                    onClick = { state.isConfirmingDeletion = true }
                ) {
                    Text(stringResource(R.string.delete))
                }
            }
        }
    }
    if (state.isConfirmingDeletion) {
        ConfirmationDialog(
            message = stringResource(R.string.message_ask_for_deleting_article),
            action = stringResource(R.string.delete),
            title = stringResource(R.string.delete_article),
        ) { agreed ->
            state.isConfirmingDeletion = false
            if (agreed) {
                state.delete()
            }
        }
    }
    state.exception?.let { exception ->
        ExceptionDialog(
            exception = exception,
            title = stringResource(R.string.delete_article),
        ) {
            state.exception = null
        }
    }
}
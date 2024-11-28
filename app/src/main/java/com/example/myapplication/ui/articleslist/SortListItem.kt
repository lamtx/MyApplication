package com.example.myapplication.ui.articleslist

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.models.SortArticleBy

@Composable
fun SortListItem(
    value: SortArticleBy,
    selectedValue: SortArticleBy?,
    onClick: () -> Unit,
) {
    val selected = value == selectedValue
    FilterChip(
        onClick = onClick,
        label = {
            Text(value.displayText())
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Composable
fun SortArticleBy.displayText(): String = when (this) {
    SortArticleBy.Index -> stringResource(R.string.index)
    SortArticleBy.Title -> stringResource(R.string.title)
    SortArticleBy.Date -> stringResource(R.string.date)
}
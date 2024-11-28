package com.example.myapplication.ui.articleslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.models.Article
import com.example.myapplication.ui.common.formatDateOnly
import com.example.myapplication.ui.common.formatLong
import com.example.myapplication.ui.theme.Dimens

@Composable
fun ArticleListItem(
    modifier: Modifier = Modifier,
    item: Article,
    onClick: (() -> Unit)? = null,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick == null) Modifier else Modifier.clickable(onClick = onClick)
            )
    ) {
        Column(
            modifier = Modifier.padding(Dimens.CardContentPadding),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = formatLong(item.index),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = formatDateOnly(item.date),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
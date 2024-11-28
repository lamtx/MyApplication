package com.example.myapplication.ui.articleslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.models.SortArticleBy
import com.example.myapplication.ui.common.ExceptionInfo
import com.example.myapplication.ui.theme.Dimens
import com.example.myapplication.ui.theme.horizontalSpacing
import com.example.myapplication.ui.theme.itemTopSpacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ArticlesList(
    state: ArticlesListViewModel = hiltViewModel(),
    onOpenArticle: (Long) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(stringResource(R.string.articles))
                },
            )
        }
    ) {

        LazyColumn(
            modifier = Modifier
                .padding(it)
        ) {
            state.exception?.let { exception ->
                item {
                    ExceptionInfo(exception, modifier = Modifier.horizontalSpacing())
                }
            }
            item {
                Row(
                    modifier = Modifier.horizontalSpacing(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(R.string.sort_by))
                    for (item in SortArticleBy.entries) {
                        SortListItem(
                            value = item,
                            selectedValue = state.sortBy,
                            onClick = { state.sort(item) }
                        )
                    }
                }
            }
            items(state.items, key = { it.index }) { item ->
                ArticleListItem(
                    modifier = Modifier
                        .horizontalSpacing()
                        .itemTopSpacing(),
                    item = item,
                    onClick = {
                        onOpenArticle(item.index)
                    },
                )
            }
        }
    }
}
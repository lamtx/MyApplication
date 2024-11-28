package com.example.myapplication.ui.articleslist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.Article
import com.example.myapplication.models.SortArticleBy
import com.example.myapplication.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesListViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
) : ViewModel() {

    val items: SnapshotStateList<Article> = mutableStateListOf()
    var exception: Exception? by mutableStateOf(null)
    var isLoading: Boolean by mutableStateOf(false)
    var sortBy: SortArticleBy? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            launch {
                articleRepository.onItemRemoved.collect { index ->
                    items.removeIf { it.index == index }
                }
            }
            launch {
                fetch()
            }
        }
    }

    private suspend fun fetch() {
        items.clear()
        isLoading = true
        try {
            items.addAll(articleRepository.listArticles(sortBy))
        } catch (e: Exception) {
            exception = e
        } finally {
            isLoading = false
        }
    }

    fun sort(sortBy: SortArticleBy) {
        this.sortBy = sortBy
        viewModelScope.launch {
            fetch()
        }
    }
}
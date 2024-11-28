package com.example.myapplication.repository

import com.example.myapplication.models.Article
import com.example.myapplication.models.SortArticleBy
import com.example.myapplication.repository.datasource.Cloud
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val cloud: Cloud,
) {
    /**
     * To simulate the "Delete" function, we cache the list of articles
     * and delete it locally.
     */
    private var cacheArticles: MutableList<Article>? = null

    private val _onItemRemoved: MutableSharedFlow<Long> = MutableSharedFlow<Long>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val onItemRemoved: SharedFlow<Long> get() = _onItemRemoved.asSharedFlow()

    private suspend fun listInternal(): MutableList<Article> {
        return cacheArticles ?: cloud.listArticles().toMutableList().also {
            cacheArticles = it
        }
    }

    suspend fun listArticles(sortBy: SortArticleBy?): List<Article> {
        val items = listInternal()
        if (sortBy != null) {
            items.sortWith(sortBy.comparator)
        }
        return items
    }

    suspend fun getArticle(index: Long): Article {
        val items = listInternal()
        return items.first { it.index == index }
    }

    suspend fun deleteArticle(index: Long) {
        val items = listInternal()
        items.removeIf { it.index == index }
        _onItemRemoved.tryEmit(index)
    }

    companion object {
        private val SortArticleBy.comparator: Comparator<Article>
            get() = when (this) {
                SortArticleBy.Index -> compareByDescending { it.index }
                SortArticleBy.Title -> compareByDescending { it.title }
                SortArticleBy.Date -> compareByDescending { it.date }
            }
    }
}
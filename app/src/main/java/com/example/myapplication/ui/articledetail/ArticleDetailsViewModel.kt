package com.example.myapplication.ui.articledetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.Article
import com.example.myapplication.repository.ArticleRepository
import com.example.myapplication.ui.MainDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository,
) : ViewModel() {
    val index: Long = savedStateHandle[MainDestinations.KEY_INDEX]
        ?: error("index is required")
    var isLoading by mutableStateOf(false)
        private set
    var article: Article? by mutableStateOf(null)
        private set
    var errorContent: Exception? by mutableStateOf(null)
        private set
    var isConfirmingDeletion by mutableStateOf(false)
    var isFinishRequested by mutableStateOf(false)
    var exception: Exception? by mutableStateOf(null)

    init {
        load()
    }

    private fun load() {
        isLoading = true
        viewModelScope.launch {
            try {
                article = articleRepository.getArticle(index)
            } catch (e: Exception) {
                errorContent = e
            } finally {
                isLoading = false
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            try {
                articleRepository.deleteArticle(index)
            } catch (e: Exception) {
                exception = e
                return@launch
            }
            isFinishRequested = true
        }
    }
}
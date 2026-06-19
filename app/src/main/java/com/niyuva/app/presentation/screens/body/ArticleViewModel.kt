package com.niyuva.app.presentation.screens.body

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.niyuva.app.data.local.content.LocalArticleRepository
import com.niyuva.app.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val localArticleRepository: LocalArticleRepository
) : ViewModel() {

    private val _article = MutableStateFlow<Article?>(null)
    val article: StateFlow<Article?> = _article.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        val topicId: String? = savedStateHandle["topicId"]
        if (topicId != null) {
            _article.value = localArticleRepository.getArticle(topicId)
        }
        _isLoading.value = false
    }
}

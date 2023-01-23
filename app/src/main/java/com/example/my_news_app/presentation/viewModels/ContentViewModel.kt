package com.example.my_news_app.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.use_case.delete_saved_news_use_case.DeleteSavedNewsUseCase
import com.example.my_news_app.domain.use_case.insert_article_use_case.InsertArticleIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase,
    private val insertArticleIUseCase: InsertArticleIUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<List<Article>?> = MutableStateFlow(null)
    private val _isFav: MutableStateFlow<Pair<Long?, Boolean>> = MutableStateFlow(Pair(-1, false))
    val state: StateFlow<List<Article>?>
        get() = _state
    val isFav: StateFlow<Pair<Long?, Boolean>>
        get() = _isFav

    fun initArtile(article: Article?) {
        article?.run {
            _isFav.update { Pair(first = id, second = isFav) }
        }
    }

    fun handleSaveClick(article: Article?) {
        if (isFav.value.second.not()) article?.let { insert(it) }
        if (isFav.value.second) article?.let { delete(it) }
    }

    private fun insert(article: Article) = viewModelScope.launch {
        val id = insertArticleIUseCase(article)
        _isFav.update { Pair(id, true) }
    }

    private fun delete(article: Article) = viewModelScope.launch {
        val id =
            deleteSavedNewsUseCase(article.copy(id = isFav.value.first, isFav = isFav.value.second))
        _isFav.update { Pair(null, false) }
    }
}
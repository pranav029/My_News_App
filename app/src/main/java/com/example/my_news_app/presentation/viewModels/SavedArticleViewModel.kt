package com.example.my_news_app.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.use_case.delete_saved_news_use_case.DeleteSavedNewsUseCase
import com.example.my_news_app.domain.use_case.get_saved_news_use_case.GetSavedNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedArticleViewModel @Inject constructor(
    private val getSavedNewsUseCase: GetSavedNewsUseCase,
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<List<Article>?> = MutableStateFlow(null)
    val state: StateFlow<List<Article>?>
        get() = _state

    init {
        getArticle()
    }

    fun getArticle() = viewModelScope.launch {
        getSavedNewsUseCase().collectLatest { article ->
            _state.update { article }
        }
    }

    fun deleteArticle(article: Article?) {
        article?.let {
            viewModelScope.launch { deleteSavedNewsUseCase.invoke(it) }
        }
    }
}
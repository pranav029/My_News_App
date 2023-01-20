package com.example.my_news_app.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.use_case.delete_saved_news_use_case.DeleteSavedNewsUseCase
import com.example.my_news_app.domain.use_case.insert_article_use_case.InsertArticleIUseCase
import com.example.my_news_app.presentation.uistate.MainFragmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase,
    private val insertArticleIUseCase: InsertArticleIUseCase
): ViewModel() {
    private val _state: MutableStateFlow<List<Article>?> = MutableStateFlow(null)
    val state: StateFlow<List<Article>?>
        get() = _state

    fun handleSaveClick(article: Article?) = viewModelScope.launch {
        article?.run {
            if(isFav.not()) insertArticleIUseCase(this)
            if (isFav)deleteSavedNewsUseCase(this)
        }?:return@launch
    }
}
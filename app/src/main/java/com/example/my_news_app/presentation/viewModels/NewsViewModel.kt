package com.example.my_news_app.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_news_app.constants.Constants.ENTERTAINMENT_NEWS
import com.example.my_news_app.constants.Constants.GENERAL_NEWS
import com.example.my_news_app.constants.Constants.SPORTS_NEWS
import com.example.my_news_app.constants.Constants.TECHNOLOGY_NEWS
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.use_case.delete_saved_news_use_case.DeleteSavedNewsUseCase
import com.example.my_news_app.domain.use_case.get_selected_news.GetSelectedNewsUseCase
import com.example.my_news_app.domain.use_case.insert_article_use_case.InsertArticleIUseCase
import com.example.my_news_app.presentation.uistate.MainFragmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getSelectedNewsUseCase: GetSelectedNewsUseCase,
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase,
    private val insertArticleIUseCase: InsertArticleIUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<MainFragmentState> = MutableStateFlow(MainFragmentState())
    val state: StateFlow<MainFragmentState>
        get() = _state

    init {
        getNews(GENERAL_NEWS)
    }
    fun handleSaveIconClick(article: Article?) = viewModelScope.launch{
        article?.run {
            if(isFav.not()) insertArticleIUseCase(this)
            if (isFav)deleteSavedNewsUseCase(this)
        }?:return@launch
    }

    fun handleCategoryCheck(checkedText: CharSequence, checkedId: Int) {
        if (checkedId == state.value.selectedChipId) return
        else _state.update { oldState -> oldState.copy(selectedChipId = checkedId) }
        val category = checkedText.toString().lowercase()
        when {
            category.equals(GENERAL_NEWS) -> getNews(GENERAL_NEWS)
            category.equals(ENTERTAINMENT_NEWS) -> getNews(ENTERTAINMENT_NEWS)
            category.equals(SPORTS_NEWS) -> getNews(SPORTS_NEWS)
            category.equals(TECHNOLOGY_NEWS) -> getNews(TECHNOLOGY_NEWS)
            else -> Unit
        }
    }

    private fun getNews(category: String) =
        getSelectedNewsUseCase(category).onEach {
            _state.update { oldState -> oldState.copy(it) }
        }.launchIn(viewModelScope)
}
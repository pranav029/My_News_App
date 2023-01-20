package com.example.my_news_app.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.use_case.get_search_news.GetSearchNewsUseCase
import com.example.my_news_app.utils.ResponseType
import com.example.my_news_app.utils.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchNewsUseCase: GetSearchNewsUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    private val _result: MutableStateFlow<List<Article>?> = MutableStateFlow(null)
    val state: StateFlow<SearchState>
        get() = _state
    val result: StateFlow<List<Article>?>
        get() = _result

    fun handleTextUpdate(query: String) {
        if (getSearchNewsUseCase.canMakeQuery(query)) searchNews(query.trim())
        _state.update { currentState ->
            currentState.copy(
                isCancelIconVisible = query.isNotEmpty(),
                queryText = query.ifEmpty { null })
        }
        if (query.trim().isEmpty()) _result.update { emptyList() }
    }

    private fun searchNews(search: String) = getSearchNewsUseCase(search).onEach { response ->
        when (response) {
            is ResponseType.Loading -> _state.update { currentState ->
                currentState.copy(
                    isCancelIconVisible = false,
                    isSearchProgressVisible = true
                )
            }
            is ResponseType.Failure -> _state.update { currentState ->
                currentState.copy(
                    isCancelIconVisible = currentState.queryText.isNullOrEmpty().not(),
                    isSearchProgressVisible = false
                )
            }
            is ResponseType.Success -> _result.update { response.data }
                .also {
                    _state.update { currentState ->
                        currentState.copy(
                            isSearchProgressVisible = false,
                            isEmptyMessageVisible = response.data.isNullOrEmpty() && !state.value.queryText.isNullOrEmpty()
                        )
                    }
                }
        }
    }.launchIn(viewModelScope)
}
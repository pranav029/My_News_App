package com.example.my_news_app.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_news_app.constants.Constants
import com.example.my_news_app.domain.use_case.get_selected_news.GetSelectedNewsUseCase
import com.example.my_news_app.presentation.uistate.MainFragmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getSelectedNewsUseCase: GetSelectedNewsUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<MainFragmentState> = MutableStateFlow(MainFragmentState())
    val state: StateFlow<MainFragmentState>
        get() = _state

    init {
        getNews(Constants.GENERAL_NEWS)
    }

    fun getNews(category: String) =
        getSelectedNewsUseCase(category).onEach {
            _state.update { oldState -> oldState.copy(it) }
        }.launchIn(viewModelScope)
}
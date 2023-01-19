package com.example.my_news_app.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.my_news_app.presentation.uistate.MainActivityState
import com.example.my_news_app.utils.SearchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<MainActivityState>(MainActivityState())
    private val _queryState = MutableStateFlow<String?>(null)
    private val _queryEvent = MutableStateFlow<SearchEvent>(SearchEvent.idle())
    val state: StateFlow<MainActivityState>
        get() = _state
    val queryState: StateFlow<String?>
        get() = _queryState
    val queryEvent: StateFlow<SearchEvent>
        get() = _queryEvent


    fun showProgressDialog() =
        _state.update { currentState -> currentState.copy(isProgressDialogVisible = true) }

    fun hideProgressDialog() =
        _state.update { currentState -> currentState.copy(isProgressDialogVisible = false) }


    fun hideAppBar() =
        _state.update { currentState -> currentState.copy(isAppbarVisible = false) }

    fun showAppBar() =
        _state.update { currentState -> currentState.copy(isAppbarVisible = true) }

    fun showBottomNav() =
        _state.update { currentState -> currentState.copy(isBottomNavVisible = true) }

    fun hideBottomNav() =
        _state.update { currentState -> currentState.copy(isBottomNavVisible = false) }

    fun showSearchGroup() =
        _state.update { currentState -> currentState.copy(isSearchGroupVisible = true) }

    fun hideSearchGroup() =
        _state.update { currentState -> currentState.copy(isSearchGroupVisible = false) }

    fun handleTextUpdate(text: String) =
        if (text.trim().length >= 3 && text.trim()
                .isNotEmpty()
        ) _queryState.update { text.trim() } else _queryState.update { null }
            .also { _queryEvent.update { SearchEvent.idle(isCancelIconVisible = text.isNotEmpty()) } }

    fun onProcessQuery() =
        _queryEvent.update { SearchEvent.onProcessQuery() }


    fun onQueryResult() =
        _queryEvent.update { SearchEvent.onQueryResult() }
}
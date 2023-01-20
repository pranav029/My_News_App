package com.example.my_news_app.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.my_news_app.presentation.uistate.MainActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<MainActivityState>(MainActivityState())
    val state: StateFlow<MainActivityState>
        get() = _state


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
}
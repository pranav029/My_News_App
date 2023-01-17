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
        _state.update { oldState -> oldState.copy(isProgressDialogVisible = true) }

    fun hideProgressDialog() =
        _state.update { oldState -> oldState.copy(isProgressDialogVisible = false) }

    fun searchIconClicked() =
        _state.update { oldState -> oldState.copy(searchIconClicked = true) }

}
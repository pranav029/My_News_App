package com.example.my_news_app.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.use_case.get_search_news.GetSearchNewsUseCase
import com.example.my_news_app.utils.ResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchNewsUseCase: GetSearchNewsUseCase
) : ViewModel() {

    private val _result: MutableLiveData<ResponseType<List<Article>>> = MutableLiveData(null)
    val result: LiveData<ResponseType<List<Article>>> = _result

    fun handleTextUpdate(queryState: StateFlow<String?>) = viewModelScope.launch {
        queryState.collectLatest { state ->
            state?.let { query -> searchNews(query) } ?: _result.postValue(
                ResponseType.Success(null)
            )
        }
    }

    private fun searchNews(search: String) = getSearchNewsUseCase(search).onEach {
        _result.value = it
    }.launchIn(viewModelScope)
}
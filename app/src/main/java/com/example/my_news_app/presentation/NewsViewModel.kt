package com.example.my_news_app.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.use_case.get_selected_news.GetSelectedNewsUseCase
import com.example.my_news_app.utils.Constants.GENERAL_NEWS
import com.example.my_news_app.utils.ResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getSelectedNewsUseCase: GetSelectedNewsUseCase
) : ViewModel() {
    val result: MutableLiveData<ResponseType<List<Article>>> = MutableLiveData()
    var articleUrl: String? = null
    private val _showProgressDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _showDetailsNewsFragment: MutableLiveData<Boolean> = MutableLiveData(false)
    val showDetail = _showDetailsNewsFragment
    val progressDialogVisible = _showProgressDialog


    init {
        getNews(GENERAL_NEWS)
    }

    fun getNews(category: String) =
        getSelectedNewsUseCase(category).onEach {
            result.postValue(it)
        }.launchIn(viewModelScope)

    fun articleClick(articleUrl: String) {
        this.articleUrl = articleUrl
        enableShowDetailsFragment()
    }

    fun showProgressDialog() = _showProgressDialog.postValue(true)
    fun hideProgressDialog() = _showProgressDialog.postValue(false)
    fun disableShowDetailsFragment() {
        _showDetailsNewsFragment.value = false
        articleUrl = null
    }

    fun enableShowDetailsFragment() {
        _showDetailsNewsFragment.value = true
    }


}
package com.example.my_news_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.my_news_app.repositories.NewsRepo

class NewsViewModelProviderFactory(
    private val newsrepo: NewsRepo
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsrepo) as T
    }
}
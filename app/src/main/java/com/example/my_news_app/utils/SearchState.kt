package com.example.my_news_app.utils

import com.example.my_news_app.domain.model.Article

data class SearchState(
    val isCancelIconVisible:Boolean = false,
    val isSearchProgressVisible:Boolean = false,
    val isEmptyMessageVisible:Boolean = false,
    val queryText:String? = null
)
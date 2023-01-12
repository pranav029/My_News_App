package com.example.my_news_app.presentation.uistate

import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.utils.ResponseType
import com.example.my_news_app.utils.ViewType

data class MainFragmentState(
    val response:ResponseType<List<Article>> = ResponseType.Loading()
)

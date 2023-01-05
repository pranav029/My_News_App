package com.example.my_news_app.data.remote.dto

data class ResponseArticlesDto(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
)
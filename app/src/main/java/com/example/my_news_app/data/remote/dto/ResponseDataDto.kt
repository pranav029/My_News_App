package com.example.my_news_app.data.remote.dto

data class ResponseDataDto(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
)
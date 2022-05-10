package com.example.my_news_app.modal

data class ResponseData(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
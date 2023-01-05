package com.example.my_news_app.domain.model

data class Article(
    val author: String?,
    val description: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)
package com.example.my_news_app.data.remote.dto

import com.example.my_news_app.domain.model.Article

data class ArticleDto(
    val author: String?=null,
    val content: String?=null,
    val description: String?=null,
    val publishedAt: String?=null,
    val source: SourceDto?=null,
    val title: String?=null,
    val url: String?=null,
    val urlToImage: String?=null
)

fun ArticleDto.toArticle():Article{
    return Article(
        author = author,
        description = description,
        title = title,
        url = url,
        urlToImage = urlToImage
    )
}
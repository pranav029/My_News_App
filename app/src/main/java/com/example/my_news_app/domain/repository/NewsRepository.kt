package com.example.my_news_app.domain.repository

import com.example.my_news_app.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews(q: String): Flow<List<Article>>
    suspend fun searchNews(search: String): Flow<List<Article>>
    suspend fun getAllArticle(): Flow<List<Article>>
    suspend fun deleteArticle(article: Article): Int
    suspend fun insertArticle(article: Article): Long
}
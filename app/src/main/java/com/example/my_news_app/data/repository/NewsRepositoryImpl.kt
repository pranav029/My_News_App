package com.example.my_news_app.data.repository

import com.example.my_news_app.Api.NewsApi
import com.example.my_news_app.data.remote.dto.ArticleDto
import com.example.my_news_app.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi
) : NewsRepository {
    override suspend fun getNews(q: String): List<ArticleDto> {
        return api.getData(q).articles
    }
}
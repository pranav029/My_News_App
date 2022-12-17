package com.example.my_news_app.domain.repository

import com.example.my_news_app.data.remote.dto.ArticleDto

interface NewsRepository {
    suspend fun getNews(q:String):List<ArticleDto>
}
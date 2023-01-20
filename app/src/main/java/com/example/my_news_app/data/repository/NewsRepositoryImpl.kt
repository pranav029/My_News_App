package com.example.my_news_app.data.repository

import android.util.Log
import com.example.my_news_app.Api.NewsApi
import com.example.my_news_app.data.remote.dto.toArticle
import com.example.my_news_app.data.room.ArticleDatabase
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val db: ArticleDatabase
) : NewsRepository {
    override suspend fun getNews(q: String): Flow<List<Article>> = flow {
        val articles = api.getArticles(q).articles.map { it.toArticle() }
        emit(articles)
    }

    override suspend fun searchNews(search: String): Flow<List<Article>> = flow {
        val articles = api.searchArticles(search).articles.map { it.toArticle() }
        emit(articles)
    }

    override suspend fun getAllArticle(): Flow<List<Article>> =
        db.articleDao().getAllArticles().map { it.map { it.toArticle() } }

    override suspend fun deleteArticle(article: Article) =
        db.articleDao().deleteArticle(article.toArticleEntity())

    override suspend fun insertArticle(article: Article) =
        db.articleDao().insertArticle(article.toArticleEntity())
}

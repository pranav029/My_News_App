package com.example.my_news_app.domain.use_case.insert_article_use_case

import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.repository.NewsRepository
import javax.inject.Inject

class InsertArticleIUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(article: Article):Long = repository.insertArticle(article)
}
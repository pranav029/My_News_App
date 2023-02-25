package com.example.my_news_app.domain.use_case.delete_saved_news_use_case

import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteSavedNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(article: Article): Int = repository.deleteArticle(article)
}
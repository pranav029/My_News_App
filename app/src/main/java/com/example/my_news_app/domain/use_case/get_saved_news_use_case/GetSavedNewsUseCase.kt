package com.example.my_news_app.domain.use_case.get_saved_news_use_case

import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetSavedNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): Flow<List<Article>> = repository.getAllArticle()
}
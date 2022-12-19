package com.example.my_news_app.domain.use_case.get_search_news

import com.example.my_news_app.domain.repository.NewsRepository
import javax.inject.Inject

class GetSearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
}
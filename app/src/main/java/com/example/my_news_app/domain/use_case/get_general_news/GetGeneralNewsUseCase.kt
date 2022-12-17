package com.example.my_news_app.domain.use_case.get_general_news

import com.example.my_news_app.domain.repository.NewsRepository
import javax.inject.Inject

class GetGeneralNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
}
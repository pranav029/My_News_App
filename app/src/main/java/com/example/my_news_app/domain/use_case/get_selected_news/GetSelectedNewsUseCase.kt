package com.example.my_news_app.domain.use_case.get_selected_news

import com.example.my_news_app.data.remote.dto.toArticle
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.repository.NewsRepository
import com.example.my_news_app.utils.ResponseType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSelectedNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(query: String): Flow<ResponseType<List<Article>>> = flow {
        try {
            emit(ResponseType.Loading())
            val response = repository.getNews(query).map { it.toArticle() }
            emit(ResponseType.Success(response))
        } catch (e: HttpException) {
            emit(
                ResponseType.Failure(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        } catch (e: IOException) {
            emit(ResponseType.Failure<List<Article>>("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(
                ResponseType.Failure<List<Article>>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        }
    }
}
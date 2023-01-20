package com.example.my_news_app.domain.use_case.get_search_news

import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.domain.repository.NewsRepository
import com.example.my_news_app.utils.ResponseType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(search: String): Flow<ResponseType<List<Article>>> = flow {
        try {
            emit(ResponseType.Loading())
            repository.searchNews(search)
                .combine(repository.getAllArticle()) { articleFromApi, articleFromDatabase ->
                    val response = articleFromApi.map {
                        if (articleFromDatabase.contains(it)) it.copy(
                            isFavVisible = true,
                            isFav = true
                        )
                        else it.copy(isFavVisible = true)
                    }
                    emit(ResponseType.Success(response))
                }.collect()
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

    fun canMakeQuery(query: String) = query.trim().isNotEmpty() && query.trim().length >= 3
}
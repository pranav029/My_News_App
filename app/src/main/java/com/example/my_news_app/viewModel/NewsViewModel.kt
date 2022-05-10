package com.example.my_news_app.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_news_app.modal.Article
import com.example.my_news_app.repositories.NewsRepo
import com.example.my_news_app.utils.ResponseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(
    private val newsRepo: NewsRepo
):ViewModel() {
    val result:MutableLiveData<ResponseType<ArrayList<Article>>> = MutableLiveData()

    fun getNews(category:String) = viewModelScope.launch(Dispatchers.IO) {
        result.postValue(ResponseType.Loading())
        val response = newsRepo.fetchNews(category)
        if(response.isSuccessful){
            response.body()?.let {
                val dispItem:ArrayList<Article> = ArrayList();
                dispItem.addAll(it.articles)
                result.postValue(ResponseType.Success(dispItem))
            }
        }else{
            result.postValue(ResponseType.Failure(response.message()))
        }
    }

}
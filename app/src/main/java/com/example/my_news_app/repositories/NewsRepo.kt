package com.example.my_news_app.repositories

import androidx.lifecycle.MutableLiveData
import com.example.my_news_app.model.Article
import com.example.my_news_app.model.ResponseData
import com.example.my_news_app.Api.retrofitInstance
import retrofit2.Call
import retrofit2.Response

class NewsRepo {
    var result:MutableLiveData<ArrayList<Article>> = MutableLiveData()
     suspend fun fetchNews(category:String): Response<ResponseData> {
        return retrofitInstance.get.getData(category)
    }
}
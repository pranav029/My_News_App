package com.example.my_news_app.Api

import com.example.my_news_app.modal.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface myApi {
//    @GET("v2/everything?sortBy=popularity&apiKey=d510d2e3544946238b7647a2f0239d1c")
//    fun getData(): retrofit2.Call<ResponseData>

    @GET("v2/top-headlines?language=en&apiKey=d510d2e3544946238b7647a2f0239d1c")
    suspend fun getData(@Query("category") name:String): Response<ResponseData>
}
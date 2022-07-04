package com.example.my_news_app.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://newsapi.org/")
                .build()
        }
        val get: MyApi by lazy {
            retrofit.create(MyApi::class.java)
        }
    }
}

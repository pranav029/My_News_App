package com.example.my_news_app.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class retrofitInstance {
    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://newsapi.org/")
                .build()
        }
        val get by lazy {
            retrofit.create(myApi::class.java)
        }
    }
}
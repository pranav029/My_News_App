package com.example.my_news_app.di

import com.example.my_news_app.Api.NewsApi
import com.example.my_news_app.constants.Constants.BASE_URL
import com.example.my_news_app.constants.Constants.CALL_TIMEOUT
import com.example.my_news_app.constants.Constants.CONNECTION_TIMEOUT
import com.example.my_news_app.constants.Constants.READ_TIMEOUT
import com.example.my_news_app.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp3Client(): OkHttpClient.Builder =
        OkHttpClient.Builder().callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())


    @Provides
    @Singleton
    fun provideNewsApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpBuilder: OkHttpClient.Builder
    ): NewsApi =
        retrofitBuilder.baseUrl(BASE_URL)
            .client(okHttpBuilder.addInterceptor(AuthInterceptor()).build())
            .build().create(NewsApi::class.java)
}
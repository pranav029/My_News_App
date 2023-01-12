package com.example.my_news_app.network

import com.example.my_news_app.BuildConfig
import com.example.my_news_app.constants.Constants.AUTHORIZATION_HEADER
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .header(AUTHORIZATION_HEADER, BuildConfig.API_KEY)
            .build()
        return chain.proceed(request)
    }
}
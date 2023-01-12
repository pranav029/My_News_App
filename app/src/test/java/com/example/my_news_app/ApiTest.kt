package com.example.my_news_app

import com.example.my_news_app.Api.NewsApi
import com.example.my_news_app.constants.Constants
import com.example.my_news_app.network.AuthInterceptor
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiTest {
    private lateinit var api: NewsApi

    @Before
    fun `init`() {
        api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(
                OkHttpClient.Builder().callTimeout(Constants.CALL_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(AuthInterceptor()).build()
            )
            .build().create(NewsApi::class.java)
    }

    @Test
    fun `Test api class`() = runBlocking {
        val result = api.getArticles("sports")
        assertNotNull(result)
    }

    @Test
    fun `Test 2`() = runBlocking {
        val result = api.getArticles("business")
        assertEquals(result.status, "ok")
    }

    @Test
    fun `Test 3`() = runBlocking {
        val result = api.getArticles("bus")
        assertEquals(result.totalResults, 0)
    }
}
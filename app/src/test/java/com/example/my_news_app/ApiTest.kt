package com.example.my_news_app

import com.example.my_news_app.Api.NewsApi
import com.example.my_news_app.utils.Constants
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiTest {
    private lateinit var api:NewsApi

    @Before
    fun `init`(){
            api = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build().create(NewsApi::class.java)
    }
    @Test
    fun `Test api class`() = runBlocking {
        val result = api.getData("sports")
        assertNotNull(result)
    }

    @Test
    fun `Test 2`() = runBlocking {
        val result = api.getData("business")
        assertEquals(result.status,"ok")
    }

    @Test
    fun `Test 3`() = runBlocking {
        val result = api.getData("bus")
        assertEquals(result.totalResults,0)
    }
}
package com.example.my_news_app

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.my_news_app.storage.Article
import com.example.my_news_app.storage.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(),forApi {
    lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()
        val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.viewer,opt(this,this))
            transaction.commit()
        transaction.replace(R.id.fragment,Display(this,ArrayList<Article>(),true))
         fetchdata("general")
    }


    override fun fetchdata(category: String) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment,Display(this@MainActivity, ArrayList<Article>(),true))
        transaction.commit()
        val url = "https://newsapi.org/"
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
            .create(myApi::class.java)
        val retrofitdata= retrofit.getData(category)
        retrofitdata.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(
                call: Call<ResponseData?>,
                response: retrofit2.Response<ResponseData?>
            ) {
                val resbody=response.body()!!
                val stringbuilder=StringBuilder()
                if (resbody != null) {
                    stringbuilder.append(resbody.status)
                }
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                val dispItem=ArrayList<Article>()
                for(items in resbody.articles){
                    dispItem.add(items)
                }
                transaction.replace(R.id.fragment,Display(this@MainActivity,dispItem,false))
                transaction.commit()
//                Toast.makeText(this@MainActivity,dispItem.size.toString(),Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Toast.makeText(this@MainActivity,t.message,Toast.LENGTH_LONG).show()
            }
        })
    }



}
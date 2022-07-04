package com.example.my_news_app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.my_news_app.R
import com.example.my_news_app.fragments.Display
import com.example.my_news_app.api.MyApi
import com.example.my_news_app.fragments.opt
import com.example.my_news_app.repositories.NewsRepo
import com.example.my_news_app.utils.MyFragmentFactory
import com.example.my_news_app.utils.ResponseType
import com.example.my_news_app.viewModel.NewsViewModelProviderFactory
import com.example.my_news_app.viewModel.NewsViewModel


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    var transaction:FragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MyFragmentFactory(this,ArrayList(),true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()
        val viewModelProviderFactory = NewsViewModelProviderFactory(NewsRepo())
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)
        transaction.beginTransaction().replace(R.id.viewer, opt(this)).commit()
        viewModel.result.observe(this, Observer { response ->
        when(response){
            is ResponseType.Success->{
                response.data?.let{transaction.beginTransaction().replace(R.id.fragment,Display(it,false)).commit()}
//                    Toast.makeText(this,"Reached here",Toast.LENGTH_LONG).show()
            }
            is ResponseType.Failure->{

            }
            is ResponseType.Loading->{
                transaction.beginTransaction().replace(R.id.fragment,Display(ArrayList(),true)).commit()
            }
        }
    })
        getNews("general")
    }

    fun getNews(category:String) = viewModel.getNews(category)

}
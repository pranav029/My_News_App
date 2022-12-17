package com.example.my_news_app.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.my_news_app.R
import com.example.my_news_app.presentation.fragments.Display
import com.example.my_news_app.presentation.fragments.opt
import com.example.my_news_app.utils.MyFragmentFactory
import com.example.my_news_app.utils.ResponseType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ClickCallBack {
    private val viewModel: NewsViewModel by viewModels()
    var transaction = supportFragmentManager.beginTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MyFragmentFactory(this, ArrayList(), true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()
        transaction.replace(R.id.viewer, opt(this))
        transaction.commit()
        viewModel.result.observe(this, Observer { response ->
            transaction = supportFragmentManager.beginTransaction()
            when (response) {
                is ResponseType.Success -> {
                    response.data?.let {
                        transaction.replace(
                            R.id.fragment,
                            Display(it, false, this)
                        )
                    }
                }
                is ResponseType.Failure -> {
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                }
                is ResponseType.Loading -> {
                    transaction.replace(R.id.fragment, Display(ArrayList(), true, this))
                }
            }
            transaction.commit()
        })
    }

    override fun onCategoryClick(category: String) = viewModel.getNews(category)

    override fun onArticleClick(url: String) {
        val intent = Intent(this, FullNews::class.java)
        intent.putExtra("URL", url)
        this.startActivity(intent)
    }
}
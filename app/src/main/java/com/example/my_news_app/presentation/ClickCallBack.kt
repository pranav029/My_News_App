package com.example.my_news_app.presentation

interface ClickCallBack {
    fun onCategoryClick(category:String)
    fun onArticleClick(url:String)
}
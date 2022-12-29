package com.example.my_news_app.utils

import com.example.my_news_app.domain.model.Article

const val VIEW_TYPE_HEADER = 1
const val VIEW_TYPE_ARTICLE = 2
const val VIEW_TYPE_TOP_NEWS = 3

sealed class ViewType(
    val article: com.example.my_news_app.domain.model.Article? = null,
    val list: List<com.example.my_news_app.domain.model.Article>? = null,
    val heading:String? = null,
    val viewType:Int
) {
    class Header(heading: String) : ViewType(heading = heading, viewType =  VIEW_TYPE_HEADER)
    class Article(article: com.example.my_news_app.domain.model.Article) :
        ViewType(article= article, viewType = VIEW_TYPE_ARTICLE)

    class TopNews(list: List<com.example.my_news_app.domain.model.Article>) :
        ViewType(list = list, viewType = VIEW_TYPE_TOP_NEWS)
}

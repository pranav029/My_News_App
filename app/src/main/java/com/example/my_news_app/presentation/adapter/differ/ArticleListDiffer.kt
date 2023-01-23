package com.example.my_news_app.presentation.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import com.example.my_news_app.utils.ViewType


class ArticleListDiffer: DiffUtil.ItemCallback<ViewType>() {


    override fun areItemsTheSame(oldItem: ViewType, newItem: ViewType): Boolean {
        return when (oldItem) {
            is ViewType.Article -> oldItem.article?.url.equals(
                newItem.article?.url
            )
            is ViewType.Header -> true
            is ViewType.TopNews -> true
        }
    }

    override fun areContentsTheSame(oldItem: ViewType, newItem: ViewType): Boolean {
        return when (oldItem) {
            is ViewType.Article ->
                oldItem.article == newItem.article
            is ViewType.Header -> true
            is ViewType.TopNews ->
                oldItem.list == newItem.list
        }
    }
}


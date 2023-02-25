package com.example.my_news_app.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.constants.Constants
import com.example.my_news_app.databinding.ItemTopNewsBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.utils.UiHelper.Companion.loadImageFromUrl

class TopNewsPagerViewHolder(private val mBinding: ItemTopNewsBinding) :
    RecyclerView.ViewHolder(mBinding.root) {
    fun bind(
        article: Article,
        onItemClick: (article: Article, sharedElements: List<Pair<View, String>>) -> Unit
    ) = mBinding.run {
        article.urlToImage?.let {
            root.context.loadImageFromUrl(it, imageview)
                .also {
                    imageview.visibility = View.VISIBLE
                    imageview.transitionName = Constants.LIST_IMAGE + bindingAdapterPosition
                }
        } ?: run { imageview.visibility = View.GONE }

        article.title?.let { heading.text = it }

        root.setOnClickListener() {
            onItemClick(
                article, listOf(
                    Pair(imageview, Constants.CONTENT_IMAGE), Pair(
                        heading,
                        Constants.CONTENT_HEADING
                    )
                )
            )
        }
    }
}
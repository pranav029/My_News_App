package com.example.my_news_app.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.constants.Constants.TOP_NEWS_OFFSCREEN_LIMIT
import com.example.my_news_app.databinding.ItemViewPagerBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.adapter.TopNewsPagerAdapter
import com.example.my_news_app.utils.AnimationUtil.Companion.fadeInAnimation
import com.example.my_news_app.utils.UiHelper.Companion.addCustomTransformer

class TopArticlesViewHolder(private val mBinding: ItemViewPagerBinding) :
    RecyclerView.ViewHolder(mBinding.root) {
    fun bind(
        articles: List<Article>?,
        onItemClick: (article: Article, sharedElements: List<Pair<View, String>>) -> Unit,
        lastAnimatePosition: Int,
        updateLastAnimatePosition: (Int) -> Unit
    ) {
        articles?.let {
            with(mBinding.llViewpager) {
                addCustomTransformer()
                adapter = TopNewsPagerAdapter(it, onItemClick)
                offscreenPageLimit = TOP_NEWS_OFFSCREEN_LIMIT
                clipToPadding = false
//                clipChildren = false
                getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            }
        }
        if (bindingAdapterPosition > lastAnimatePosition) {
            mBinding.llViewpager.fadeInAnimation()
            updateLastAnimatePosition(bindingAdapterPosition)
        }
    }

    fun getViewPagerCurrentItem(): Int = mBinding.llViewpager.currentItem
    fun setViewPagerCurrentItem(position: Int) {
        mBinding.llViewpager.currentItem = position
    }
}
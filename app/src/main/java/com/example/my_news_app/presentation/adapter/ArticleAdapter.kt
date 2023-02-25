package com.example.my_news_app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.my_news_app.databinding.CardViewBinding
import com.example.my_news_app.databinding.ItemHeaderBinding
import com.example.my_news_app.databinding.ItemViewPagerBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.adapter.differ.ArticleListDiffer
import com.example.my_news_app.presentation.adapter.viewholder.ArticleViewHolder
import com.example.my_news_app.presentation.adapter.viewholder.HeaderViewHolder
import com.example.my_news_app.presentation.adapter.viewholder.TopArticlesViewHolder
import com.example.my_news_app.presentation.custom.ViewPagerAutoSlider
import com.example.my_news_app.utils.VIEW_TYPE_ARTICLE
import com.example.my_news_app.utils.VIEW_TYPE_TOP_NEWS
import com.example.my_news_app.utils.ViewType

class ArticleAdapter(
    private val showImage: Boolean = true,
    private val showDelete: Boolean = false,
    private val onItemClick: (article: Article, sharedElements: List<Pair<View, String>>) -> Unit,
    private val onSaveClick: ((article: Article?) -> Unit)? = null,
    private val onDeleteClick: ((article: Article?) -> Unit)? = null

) : BaseRecyclerViewAdapter<ViewType, ArticleListDiffer>() {


    private var topNewsAutoSlider: ViewPagerAutoSlider = ViewPagerAutoSlider()
    private var lastPosition = -1

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        topNewsAutoSlider.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is TopArticlesViewHolder) {
            topNewsAutoSlider.onViewPagerHidden()
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is TopArticlesViewHolder) {
            topNewsAutoSlider.onViewPagerVisible(
                holder::getViewPagerCurrentItem,
                holder::setViewPagerCurrentItem
            )
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        topNewsAutoSlider.onDetach()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when {
            viewType.equals(VIEW_TYPE_ARTICLE) -> {
                val mBinding =
                    CardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ArticleViewHolder(mBinding)
            }
            viewType.equals(VIEW_TYPE_TOP_NEWS) -> {
                val mBinding =
                    ItemViewPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TopArticlesViewHolder(mBinding)
            }
            else -> {
                val mBinding =
                    ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(mBinding)
            }
        }
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, item: ViewType, position: Int) {
        when (item) {
            is ViewType.Article ->
                (holder as ArticleViewHolder).bind(
                    item.article,
                    showImage,
                    showDelete,
                    onSaveClick,
                    onDeleteClick,
                    onItemClick,
                    lastPosition
                ) { newPosition -> lastPosition = newPosition }
            is ViewType.TopNews ->
                (holder as TopArticlesViewHolder).bind(
                    item.list,
                    onItemClick,
                    lastPosition
                ) { newPosition -> lastPosition = newPosition }
            is ViewType.Header ->
                (holder as HeaderViewHolder).bind(item.heading, lastPosition) { newPosition ->
                    lastPosition = newPosition
                }
        }
    }


    override fun getItemViewType(position: Int): Int = mList[position].viewType


    override fun getDiffer(): ArticleListDiffer = ArticleListDiffer()
}

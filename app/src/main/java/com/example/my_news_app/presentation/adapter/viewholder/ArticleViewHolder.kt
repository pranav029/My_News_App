package com.example.my_news_app.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.R
import com.example.my_news_app.constants.Constants
import com.example.my_news_app.databinding.CardViewBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.utils.AnimationUtil.Companion.slideInAnimation
import com.example.my_news_app.utils.UiHelper.Companion.loadImageFromUrl

class ArticleViewHolder(private val mBinding: CardViewBinding) :
    RecyclerView.ViewHolder(mBinding.root) {
    fun bind(
        article: Article?,
        showImage: Boolean,
        showDelete: Boolean,
        onSaveClick: ((article: Article?) -> Unit)? = null,
        onDeleteClick: ((article: Article?) -> Unit)? = null,
        onItemClick: (article: Article, sharedElements: List<Pair<View, String>>) -> Unit,
        lastAnimatePosition: Int,
        updateLastAnimatePosition: (Int) -> Unit
    ) = mBinding.run {
        article?.run {
            if (showImage) urlToImage?.let {
                root.context.loadImageFromUrl(it, imageview)
                    .also { imageview.visibility = View.VISIBLE }
            }
            else imageview.visibility = View.GONE

            title?.let { heading.text = it }
            time?.let { tvTime.text = it }
            ivFavourite.isVisible = isFavVisible
            ivFavourite.setImageDrawable(
                ContextCompat.getDrawable(
                    root.context,
                    if (isFav) R.drawable.ic_saved else R.drawable.ic_unsaved
                )
            )
            root.setOnClickListener() {
                val sharedElements = listOf(
                    Pair(imageview, Constants.CONTENT_IMAGE),
                    Pair(heading, Constants.CONTENT_HEADING)
                )
                onItemClick(this, sharedElements)
            }
        }
        imageview.transitionName = Constants.LIST_IMAGE + position
        heading.transitionName = Constants.LIST_HEADING + position

        if (bindingAdapterPosition > lastAnimatePosition) {
            root.slideInAnimation()
            updateLastAnimatePosition(bindingAdapterPosition)
        }
        ivFavourite.setOnClickListener { onSaveClick?.invoke(article) }
        ivDelete.isVisible = showDelete
        ivDelete.setOnClickListener { onDeleteClick?.invoke(article) }
    }
}

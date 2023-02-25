package com.example.my_news_app.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.constants.Constants.RECOMMENDED_NEWS_HEADER
import com.example.my_news_app.databinding.ItemHeaderBinding
import com.example.my_news_app.utils.AnimationUtil.Companion.slideInAnimation

class HeaderViewHolder(private val mBinding: ItemHeaderBinding) :
    RecyclerView.ViewHolder(mBinding.root) {
    fun bind(
        itemHeading: String?,
        lastAnimatePosition: Int,
        updateLastAnimatePosition: (Int) -> Unit
    ) {
        itemHeading?.let {
            with(mBinding) {
                heading.text = it
                if (it.equals(RECOMMENDED_NEWS_HEADER)) {
                    root.setPadding(0, 0, 0, 10)
                }
                if (bindingAdapterPosition > lastAnimatePosition) {
                    root.slideInAnimation()
                    updateLastAnimatePosition(bindingAdapterPosition)
                }
            }
        }
    }
}
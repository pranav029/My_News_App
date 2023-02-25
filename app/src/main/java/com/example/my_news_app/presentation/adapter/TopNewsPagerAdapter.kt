package com.example.my_news_app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.databinding.ItemTopNewsBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.adapter.viewholder.TopNewsPagerViewHolder

class TopNewsPagerAdapter(
    private val mList: List<Article>,
    private val onItemClick: (article: Article, sharedElements: List<Pair<View, String>>) -> Unit
) : RecyclerView.Adapter<TopNewsPagerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopNewsPagerViewHolder {
        val mBinding =
            ItemTopNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopNewsPagerViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: TopNewsPagerViewHolder, position: Int) =
        holder.bind(mList[position], onItemClick)


    override fun getItemCount(): Int = mList.size
}
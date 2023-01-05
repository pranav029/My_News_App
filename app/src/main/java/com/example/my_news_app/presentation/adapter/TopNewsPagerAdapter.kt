package com.example.my_news_app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.my_news_app.databinding.ItemTopNewsBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.ClickCallBack
import com.example.my_news_app.utils.UiHelper.Companion.loadImageFromUrl

class TopNewsPagerAdapter(
    private val mList:List<Article>,
    private val mListener: ClickCallBack
): RecyclerView.Adapter<TopNewsPagerAdapter.TopNewsPagerViewHolder>() {


    inner class TopNewsPagerViewHolder(val mBinding:ItemTopNewsBinding): ViewHolder(mBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopNewsPagerViewHolder {
        val mBinding = ItemTopNewsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TopNewsPagerViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: TopNewsPagerViewHolder, position: Int) {
        val article = mList[position]
        with(holder.mBinding){
           article.urlToImage?.let {
                root.context.loadImageFromUrl(it, imageview)
                    .also { imageview.visibility = View.VISIBLE }
            }?: run { imageview.visibility = View.GONE }

            article.title?.let { heading.text = it }

            root.setOnClickListener() {
                mListener.onArticleClick(article.url.toString())
            }
        }
    }

    override fun getItemCount(): Int = mList.size
}
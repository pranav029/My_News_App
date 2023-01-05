package com.example.my_news_app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.my_news_app.databinding.CardViewBinding
import com.example.my_news_app.databinding.ItemHeaderBinding
import com.example.my_news_app.databinding.ItemViewPagerBinding
import com.example.my_news_app.presentation.ClickCallBack
import com.example.my_news_app.utils.Constants.RECOMMENDED_NEWS_HEADER
import com.example.my_news_app.utils.UiHelper.Companion.addCustomTransformer
import com.example.my_news_app.utils.UiHelper.Companion.loadImageFromUrl
import com.example.my_news_app.utils.VIEW_TYPE_ARTICLE
import com.example.my_news_app.utils.VIEW_TYPE_TOP_NEWS
import com.example.my_news_app.utils.ViewType

class ArticleAdapter(
    private val mList: List<ViewType>,
    private val mListener: ClickCallBack,
    private val showImage: Boolean = true,
    private val mCallback: InfiniteScrollCallback? = null

) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is TopArticlesViewHolder) {
            mCallback?.onStartInfiniteScroll(holder.mBinding.llViewpager)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (mList[position]) {
            is ViewType.Article -> {
                val article = mList[position].article

                // sets the image to the imageview from our itemHolder class
                // holder.imageView.setImageResource(ItemsViewModel.image)

                // sets the text to the textview from our itemHolder class
                with((holder as ArticleViewHolder).mBinding) {
                    if (showImage) article?.urlToImage?.let {
                        root.context.loadImageFromUrl(it, imageview)
                            .also { imageview.visibility = View.VISIBLE }
                    }
                    else imageview.visibility = View.GONE

                    article?.title?.let { heading.text = it }

                    root.setOnClickListener() {
                        mListener.onArticleClick(article?.url.toString())
                    }
                }
            }
            is ViewType.TopNews -> {
                val articles = mList[position].list
                articles?.let {
                    with((holder as TopArticlesViewHolder).mBinding.llViewpager) {
                        addCustomTransformer()
                        adapter = TopNewsPagerAdapter(it, mListener)
                        offscreenPageLimit = 3
                        clipToPadding = false
//                clipChildren = false
                        getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                        mCallback?.onStartInfiniteScroll(this)
                    }
                }
            }
            is ViewType.Header -> {
                val itemHeading = mList[position].heading
                itemHeading?.let {
                    with((holder as HeaderViewHolder).mBinding) {
                        heading.text = it
                        if (it.equals(RECOMMENDED_NEWS_HEADER)) {
                            root.setPadding(0, 0, 0, 10)
                        }
                    }
                }
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int = mList.size

    override fun getItemViewType(position: Int): Int = mList[position].viewType

    // Holds the views for adding it to image and text
    inner class ArticleViewHolder(val mBinding: CardViewBinding) : ViewHolder(mBinding.root)
    inner class TopArticlesViewHolder(val mBinding: ItemViewPagerBinding) :
        ViewHolder(mBinding.root)

    inner class HeaderViewHolder(val mBinding: ItemHeaderBinding) : ViewHolder(mBinding.root)
}

interface InfiniteScrollCallback {
    fun onStartInfiniteScroll(viewpager: ViewPager2)
    fun onStopInfiniteScroll(viewpager: ViewPager2)
}
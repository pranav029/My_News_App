package com.example.my_news_app.presentation.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.my_news_app.R
import com.example.my_news_app.constants.Constants.CONTENT_HEADING
import com.example.my_news_app.constants.Constants.CONTENT_IMAGE
import com.example.my_news_app.constants.Constants.LIST_HEADING
import com.example.my_news_app.constants.Constants.LIST_IMAGE
import com.example.my_news_app.constants.Constants.RECOMMENDED_NEWS_HEADER
import com.example.my_news_app.constants.Constants.TOPNEWS_OFFSCREEN_LIMIT
import com.example.my_news_app.constants.Constants.VIEWPAGER_TIME_DELAY
import com.example.my_news_app.constants.Constants.VIEWPAGER_TIME_PERIOD
import com.example.my_news_app.databinding.CardViewBinding
import com.example.my_news_app.databinding.ItemHeaderBinding
import com.example.my_news_app.databinding.ItemViewPagerBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.adapter.differ.ArticleListDiffer
import com.example.my_news_app.utils.AnimationUtil.Companion.fadeInAnimation
import com.example.my_news_app.utils.AnimationUtil.Companion.slideInAnimation
import com.example.my_news_app.utils.UiHelper.Companion.addCustomTransformer
import com.example.my_news_app.utils.UiHelper.Companion.loadImageFromUrl
import com.example.my_news_app.utils.VIEW_TYPE_ARTICLE
import com.example.my_news_app.utils.VIEW_TYPE_TOP_NEWS
import com.example.my_news_app.utils.ViewType
import java.util.*

class ArticleAdapter(
    private val showImage: Boolean = true,
    private val showDelete: Boolean = false,
    private val onItemClick: (article: Article, sharedElements: List<Pair<View, String>>) -> Unit,
    private val onSaveClick: ((article: Article?) -> Unit)? = null,
    private val onDeleteClick: ((article: Article?) -> Unit)? = null

) : BaseRecyclerViewAdapter<ViewType, ArticleListDiffer>() {

    private lateinit var handler: Handler
    private var mTimer: Timer? = null
    private var mTimerTask: TimerTask? = null
    private var lastPosition = -1

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        handler = Handler(Looper.myLooper()!!)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is TopArticlesViewHolder) {
            mTimerTask?.cancel()
            mTimer?.cancel()
            mTimerTask = null
            handler.removeCallbacksAndMessages(null)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is TopArticlesViewHolder) {
            mTimer = Timer()
            mTimerTask?.cancel()
            mTimerTask = MyTimerTask(holder.mBinding.llViewpager)
            mTimer?.schedule(mTimerTask, VIEWPAGER_TIME_DELAY, VIEWPAGER_TIME_PERIOD)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mTimer?.cancel()
        mTimer = null
        handler.removeCallbacksAndMessages(null)
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
                                Pair(imageview, CONTENT_IMAGE),
                                Pair(heading, CONTENT_HEADING)
                            )
                            onItemClick(this, sharedElements)
                        }
                    }
                    imageview.transitionName = LIST_IMAGE + position
                    heading.transitionName = LIST_HEADING + position

                    if (holder.bindingAdapterPosition > lastPosition) {
                        root.slideInAnimation()
                        lastPosition = holder.bindingAdapterPosition
                    }
                    ivFavourite.setOnClickListener { onSaveClick?.invoke(article) }
                    ivDelete.isVisible = showDelete
                    ivDelete.setOnClickListener { onDeleteClick?.invoke(article) }
                }
            }
            is ViewType.TopNews -> {
                val articles = mList[position].list
                articles?.let {
                    with((holder as TopArticlesViewHolder).mBinding.llViewpager) {
                        addCustomTransformer()
                        adapter = TopNewsPagerAdapter(it, onItemClick)
                        offscreenPageLimit = TOPNEWS_OFFSCREEN_LIMIT
                        clipToPadding = false
//                clipChildren = false
                        getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                    }
                }
                if (holder.bindingAdapterPosition > lastPosition) {
                    (holder as TopArticlesViewHolder).mBinding.run {
                        llViewpager.fadeInAnimation()
                    }
                    lastPosition = holder.bindingAdapterPosition
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
                        if (holder.bindingAdapterPosition > lastPosition) {
                            root.slideInAnimation()
                            lastPosition = holder.bindingAdapterPosition
                        }
                    }
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int = mList[position].viewType

    /*
    * VIEWHOLDERS
    **/
    inner class ArticleViewHolder(val mBinding: CardViewBinding) : ViewHolder(mBinding.root)

    inner class TopArticlesViewHolder(val mBinding: ItemViewPagerBinding) :
        ViewHolder(mBinding.root)

    inner class HeaderViewHolder(val mBinding: ItemHeaderBinding) : ViewHolder(mBinding.root)

    inner class MyTimerTask(private val viewPager: ViewPager2) : TimerTask() {
        override fun run() {
            handler.post { viewPager.currentItem = (viewPager.currentItem + 1) % 3 }
        }

    }

    override fun getDiffer(): ArticleListDiffer = ArticleListDiffer()
}

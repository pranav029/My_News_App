package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.my_news_app.R
import com.example.my_news_app.databinding.FragmentMainBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.ClickCallBack
import com.example.my_news_app.presentation.NewsViewModel
import com.example.my_news_app.presentation.adapter.ArticleAdapter
import com.example.my_news_app.presentation.adapter.InfiniteScrollCallback
import com.example.my_news_app.presentation.viewModels.NewsViewModel
import com.example.my_news_app.utils.Constants.ENTERTAINMENT_NEWS
import com.example.my_news_app.utils.Constants.GENERAL_NEWS
import com.example.my_news_app.utils.Constants.RECOMMENDED_NEWS_HEADER
import com.example.my_news_app.utils.Constants.SPORTS_NEWS
import com.example.my_news_app.utils.Constants.TOP_NEWS_HEADER
import com.example.my_news_app.utils.Constants.TOP_NEWS_QUANTITY
import com.example.my_news_app.utils.ResponseType
import com.example.my_news_app.utils.ViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNewsFragment :
    Fragment(), ClickCallBack, InfiniteScrollCallback {
    private var adapter: ArticleAdapter? = null
    private var mBinding: FragmentMainBinding? = null
    private val viewModel by activityViewModels<NewsViewModel>()
    private val articles = arrayListOf<Article>()
    private lateinit var handler: Handler
    private var mTimer: Timer? = null
    private var mTimerTask: TimerTask? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        handler = Handler(Looper.myLooper()!!)
        mTimer = Timer()
        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        mBinding?.bottomNav?.setOnItemSelectedListener {
            if (it.itemId == R.id.item_general) viewModel.getNews(GENERAL_NEWS)
            if (it.itemId == R.id.item_sports) viewModel.getNews(SPORTS_NEWS)
            if (it.itemId == R.id.item_entertainment) viewModel.getNews(ENTERTAINMENT_NEWS)
            true
        }
        viewModel.result.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseType.Success -> {
                    response.data?.let {
                        articles.clear()
                        articles.addAll(it)
                        updateList()
                        viewModel.hideProgressDialog()
                    }
                }
                is ResponseType.Failure -> {
                    viewModel.hideProgressDialog()
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
                is ResponseType.Loading -> {
                    viewModel.showProgressDialog()
                }
            }
        }
    }

    private fun initRecyclerView(view: View) {
        mBinding?.recyclerview?.layoutManager = LinearLayoutManager(activity)
        adapter = ArticleAdapter(articles.map { ViewType.Article(it) }, this)
        mBinding?.recyclerview?.adapter = adapter
    }

    private fun updateList() {
        val items: ArrayList<ViewType> = arrayListOf(ViewType.Header(TOP_NEWS_HEADER))
        items.add(ViewType.TopNews(
            articles.take(
                TOP_NEWS_QUANTITY
            )
        ))
        items.add(ViewType.Header(RECOMMENDED_NEWS_HEADER))
        items.addAll(articles.drop(TOP_NEWS_QUANTITY).map { ViewType.Article(it) })
        adapter = ArticleAdapter(items, this)
        mBinding?.run {
            recyclerview.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
        adapter = null
        mTimer?.cancel()
        mTimer = null
        handler.removeCallbacksAndMessages(null)
    }

    override fun onArticleClick(url: String) = viewModel.articleClick(url)
    override fun onStartInfiniteScroll(viewpager: ViewPager2) {
        mTimerTask?.cancel()
        mTimerTask = MyTimerTask(viewpager)
        mTimer?.schedule(mTimerTask,0,3000)
    }

    override fun onStopInfiniteScroll(viewpager: ViewPager2) {
        mTimerTask?.cancel()
        mTimer?.cancel()
        mTimerTask = null
        handler.removeCallbacksAndMessages(null)
    }

    inner class MyTimerTask(private val viewPager: ViewPager2) : TimerTask() {
        override fun run() {
            handler.post { viewPager.currentItem = (viewPager.currentItem + 1) % 3 }
        }
    }
}
package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_news_app.R
import com.example.my_news_app.constants.Constants.ARTICLE
import com.example.my_news_app.constants.Constants.RECOMMENDED_NEWS_HEADER
import com.example.my_news_app.constants.Constants.TOP_NEWS_HEADER
import com.example.my_news_app.constants.Constants.TOP_NEWS_QUANTITY
import com.example.my_news_app.databinding.FragmentNewsBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.adapter.ArticleAdapter
import com.example.my_news_app.presentation.viewModels.NewsViewModel
import com.example.my_news_app.utils.ResponseType
import com.example.my_news_app.utils.ViewType
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment :
    BaseMainActivityFragment() {
    private var adapter: ArticleAdapter? = null
    private var mBinding: FragmentNewsBinding? = null
    private val viewModel by viewModels<NewsViewModel>()
    private val articles = arrayListOf<Article>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentNewsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.showAppBar()
        postponeEnterTransition()
        mBinding?.root?.viewTreeObserver?.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
        initRecyclerView(view)
        mBinding?.cgCategory?.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip? = group.findViewById(checkedId)
            chip?.run { viewModel.handleCategoryCheck(text, checkedId) }
        }
        subscribeUI()
    }

    private fun subscribeUI() = lifecycleScope.launch {
        viewModel.state.collect { uiState ->
            when (uiState.response) {
                is ResponseType.Success -> {
                    uiState.response.data?.let {
                        articles.clear()
                        articles.addAll(it)
                        updateList()
                        mainViewModel.hideProgressDialog()
                    }
                }
                is ResponseType.Failure -> {
                    mainViewModel.hideProgressDialog()
                    Toast.makeText(context, uiState.response.message, Toast.LENGTH_LONG).show()
                }
                is ResponseType.Loading -> {
                    mainViewModel.showProgressDialog()
                }
            }
        }
    }

    private fun initRecyclerView(view: View) {
        mBinding?.recyclerview?.layoutManager = LinearLayoutManager(activity)
        adapter =
            ArticleAdapter(articles.map { ViewType.Article(it) }, onItemClick = ::handleItemClick)
        mBinding?.recyclerview?.adapter = adapter
    }

    private fun updateList() {
        val items: ArrayList<ViewType> = arrayListOf(ViewType.Header(TOP_NEWS_HEADER))
        items.add(
            ViewType.TopNews(
                articles.take(
                    TOP_NEWS_QUANTITY
                )
            )
        )
        items.add(ViewType.Header(RECOMMENDED_NEWS_HEADER))
        items.addAll(articles.drop(TOP_NEWS_QUANTITY).map { ViewType.Article(it) })
        adapter = ArticleAdapter(items, onItemClick = ::handleItemClick)
        mBinding?.run {
            recyclerview.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.recyclerview?.adapter = null
        mBinding = null
        adapter = null
    }


    private fun handleItemClick(article: Article, sharedElements: List<Pair<View, String>>?) {
        val extras =
            sharedElements?.run {
                FragmentNavigator.Extras.Builder().apply {
                    forEach { pair ->
                        addSharedElement(pair.first, pair.second)
                    }
                }.build()
            }
        requireActivity().findNavController(R.id.main_nav_fragment)
            .navigate(R.id.action_NewsFragment_to_ContentFragment, Bundle().apply {
                putParcelable(
                    ARTICLE, article
                )
            }, null, extras)
    }
}
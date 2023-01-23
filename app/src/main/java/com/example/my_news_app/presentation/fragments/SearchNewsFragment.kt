package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_news_app.R
import com.example.my_news_app.constants.Constants
import com.example.my_news_app.databinding.FragmentSearchBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.adapter.ArticleAdapter
import com.example.my_news_app.presentation.viewModels.SearchViewModel
import com.example.my_news_app.utils.AnimationUtil.Companion.slideInAnimation
import com.example.my_news_app.utils.AnimationUtil.Companion.slideOutAnimation
import com.example.my_news_app.utils.UiHelper.Companion.hideKeyBoard
import com.example.my_news_app.utils.UiHelper.Companion.onTextChange
import com.example.my_news_app.utils.UiHelper.Companion.showKeyBoard
import com.example.my_news_app.utils.ViewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : BaseMainActivityFragment() {
    private var mBinding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModels()
    private var mAdapter: ArticleAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.hideBottomNav()
        mainViewModel.hideAppBar()
        mBinding?.run {
            recyclerview.layoutManager = LinearLayoutManager(activity)
            mAdapter = ArticleAdapter(onItemClick = ::handleItemClick)
            recyclerview.adapter = mAdapter
        }
        showSearchView()
        lifecycleScope.subscribeUI()
    }

    private fun CoroutineScope.subscribeUI() {
        launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->
                    mBinding?.run {
                        pbSearch.isVisible = state.isSearchProgressVisible
                        ivClearText.isVisible = state.isCancelIconVisible
                        tvNoResult.isVisible = state.isEmptyMessageVisible
                    }
                }
            }
        }
        launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.result.collectLatest { updateList(it) }
            }
        }
    }

    private fun updateList(articles: List<Article>?) = mBinding?.run {
        val resultList =
            articles?.map { ViewType.Article(it.copy(isFavVisible = true)) } ?: emptyList()
        mAdapter?.submitList(resultList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideSearchView()
        mBinding?.recyclerview?.adapter = null
        mBinding = null
        mainViewModel.showBottomNav()
        mainViewModel.showAppBar()
        mAdapter = null
    }

    private fun handleItemClick(article: Article, sharedElements: List<Pair<View, String>>?) {
        requireActivity().findNavController(R.id.main_nav_fragment)
            .navigate(R.id.action_SearchNewsFragment_to_ContentFragment, Bundle().apply {
                putParcelable(
                    Constants.ARTICLE, article
                )
            }, null, null)
    }

    private fun hideSearchView() = mBinding?.run {
        etSearch.slideOutAnimation()
        gSearch.visibility = View.GONE
        etSearch.hideKeyBoard(requireActivity())
        etSearch.text?.clear()
    }

    private fun showSearchView() = mBinding?.run {
        gSearch.isVisible = true
        pbSearch.isVisible = false
        ivClearText.isVisible = false
        etSearch.slideInAnimation()
        etSearch.requestFocus()
        etSearch.showKeyBoard(requireActivity())
        etSearch.onTextChange()
            .filter { it != null }.debounce(300).onEach {
                viewModel.handleTextUpdate(it.toString())
            }.launchIn(lifecycleScope)
        ivClearText.setOnClickListener {
            etSearch.text?.clear()
            viewModel.handleTextUpdate(etSearch.text.toString())
        }
    }
}
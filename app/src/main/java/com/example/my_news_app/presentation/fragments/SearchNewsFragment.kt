package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_news_app.R
import com.example.my_news_app.constants.Constants
import com.example.my_news_app.databinding.FragmentSearchBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.adapter.ArticleAdapter
import com.example.my_news_app.presentation.viewModels.SearchViewModel
import com.example.my_news_app.utils.ResponseType
import com.example.my_news_app.utils.ViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNewsFragment : BaseMainActivityFragment() {
    private var mBinding: FragmentSearchBinding? = null
    private val viewmodel: SearchViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel.showSearchGroup()
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.hideBottomNav()
        mBinding?.run {
            recyclerview.layoutManager = LinearLayoutManager(activity)
            subscribeUI()
        }
    }

    private fun subscribeUI() {
        viewmodel.handleTextUpdate(mainViewModel.queryState)
        viewmodel.result.observe(viewLifecycleOwner) { response ->
            mBinding?.run {
                when (response) {
                    is ResponseType.Success -> {
                        val articles =
                            response.data?.let {
                                val articles = it.map { ViewType.Article(it) }
                                if (articles.isNotEmpty()) {
                                    recyclerview.isVisible = true
                                    tvNoResult.isVisible = false
                                } else {
                                    recyclerview.isVisible = false
                                    tvNoResult.isVisible = true
                                }
                                mainViewModel.onQueryResult()
                                articles
                            }
                            //TODO handle blank field
                        recyclerview.adapter =
                            ArticleAdapter(articles ?: emptyList(), onItemClick = ::handleItemClick)
                    }
                    is ResponseType.Failure -> {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        mainViewModel.onQueryResult()
                    }
                    is ResponseType.Loading -> {
                        mainViewModel.onProcessQuery()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.recyclerview?.adapter = null
        mBinding = null
    }


    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.hideSearchGroup()
        mainViewModel.showBottomNav()
    }

    private fun handleItemClick(article: Article, sharedElements: List<Pair<View, String>>?) {
        requireActivity().findNavController(R.id.main_nav_fragment)
            .navigate(R.id.action_SearchNewsFragment_to_ContentFragment, Bundle().apply {
                putParcelable(
                    Constants.ARTICLE, article
                )
            }, null, null)
    }
}
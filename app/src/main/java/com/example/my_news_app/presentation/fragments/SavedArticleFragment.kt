package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.R
import com.example.my_news_app.constants.Constants
import com.example.my_news_app.databinding.FragmentSavedArticleBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.adapter.ArticleAdapter
import com.example.my_news_app.presentation.viewModels.SavedArticleViewModel
import com.example.my_news_app.utils.ViewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedArticleFragment : BaseMainActivityFragment() {

    private  val viewModel: SavedArticleViewModel by viewModels()
    private var mBinding:FragmentSavedArticleBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSavedArticleBinding.inflate(inflater,container,false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.showAppBar()
        mBinding?.rvSavedArticle?.layoutManager = LinearLayoutManager(activity)
        lifecycleScope.launch {
            viewModel.state.collectLatest { list->
                Log.e("Pranav",list.toString())
                val articles = list?.map { ViewType.Article(it.copy(isFavVisible = false)) }?: emptyList()
                mBinding?.rvSavedArticle?.adapter = ArticleAdapter(articles, onItemClick = ::handleItemClick)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.rvSavedArticle?.adapter = null
        mBinding = null
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
            .navigate(R.id.action_SavedArticleFragment_to_ContentFragment, Bundle().apply {
                putParcelable(
                    Constants.ARTICLE, article
                )
            }, null, extras)
    }

}
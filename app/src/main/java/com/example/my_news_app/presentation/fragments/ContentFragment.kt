package com.example.my_news_app.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.my_news_app.R
import com.example.my_news_app.constants.Constants.ARTICLE
import com.example.my_news_app.constants.Constants.ARTICLE_URL
import com.example.my_news_app.databinding.FragmentContentBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.viewModels.ContentViewModel
import com.example.my_news_app.utils.UiHelper.Companion.loadImageFromUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContentFragment : BaseMainActivityFragment() {
    private var mBinding: FragmentContentBinding? = null
    private val viewmodel: ContentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentContentBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
                .apply { duration = 500.toLong() }
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
                .apply { duration = 500.toLong() }
        arguments?.let { bundle ->
            mBinding?.run {
                bundle.getParcelable<Article>(ARTICLE)?.let { article ->
                    viewmodel.initArtile(article)
                    ctToolbar.title = article.source
                    article.urlToImage?.let { it1 ->
                        context?.loadImageFromUrl(it1, ivImage) {
                            startPostponedEnterTransition()
                        }
                    }
                    with(incContent) {
                        tvHeading.text = article.title
                        tvContent.text = article.content + article.content
                        tvDate.text = article.time
                        btnDetail.setOnClickListener {
                            findNavController().navigate(
                                R.id.action_ContentFragment_to_DetailNewsFragment,
                                Bundle().apply {
                                    putString(
                                        ARTICLE_URL,
                                        article.url
                                    )
                                })
                        }
                    }
                    ivSave.setOnClickListener {
                        viewmodel.handleSaveClick(article)
                    }
                    tToolbar.setNavigationOnClickListener { findNavController().navigateUp() }

                }
            }
        }
        subscribeUI()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun subscribeUI() = lifecycleScope.launch {
        viewmodel.isFav.collectLatest { isFav ->
            mBinding?.run {
                ivSave.foreground = ContextCompat.getDrawable(
                    ivSave.context,
                    if (isFav.second) R.drawable.ic_saved else R.drawable.ic_unsaved
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.hideBottomNav()
        mainViewModel.hideAppBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.showBottomNav()
        mainViewModel.showAppBar()
    }

}
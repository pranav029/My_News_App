package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.my_news_app.constants.Constants.ARTICLE
import com.example.my_news_app.databinding.FragmentContentBinding
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.viewModels.ContentViewModel
import com.example.my_news_app.utils.UiHelper.Companion.loadImageFromUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentFragment : BaseMainActivityFragment() {
    private var mBinding: FragmentContentBinding? = null
    private val viewModel by viewModels<ContentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentContentBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
                .apply { duration = 1000.toLong() }
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
                .apply { duration = 1000.toLong() }
        arguments?.let { bundle ->
            val article = bundle.getParcelable<Article>(ARTICLE)
            mBinding?.run {
                article?.let {
                    ctToolbar.title = it.title
                    tToolbar.title = it.title
                    it.urlToImage?.let { it1 ->
                        context?.loadImageFromUrl(it1, ivImage) {
                            startPostponedEnterTransition()
                        }
                    }
                    tvContent.text = it.content
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

}
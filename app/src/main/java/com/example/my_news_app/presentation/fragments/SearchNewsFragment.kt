package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_news_app.R
import com.example.my_news_app.constants.Constants.ARTICLE_URL
import com.example.my_news_app.databinding.FragmentSearchBinding
import com.example.my_news_app.presentation.ClickCallBack
import com.example.my_news_app.presentation.adapter.ArticleAdapter
import com.example.my_news_app.presentation.viewModels.SearchViewModel
import com.example.my_news_app.utils.ResponseType
import com.example.my_news_app.utils.ViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNewsFragment : BaseMainActivityFragment(), ClickCallBack {
    private var mBinding: FragmentSearchBinding? = null
    private val viewmodel: SearchViewModel by activityViewModels()

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
        mBinding?.run {
            recyclerview.layoutManager = LinearLayoutManager(activity)
            viewmodel.result.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is ResponseType.Success -> {
                        response.data?.let {
                            val articles = it.map { ViewType.Article(it) }
                            recyclerview.adapter = ArticleAdapter(articles, this@SearchNewsFragment)
                        }
                    }
                    is ResponseType.Failure -> {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                    is ResponseType.Loading -> {

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }


    override fun onArticleClick(url: String) = findNavController().navigate(
        R.id.action_SearchNewsFragment_to_DetailNewsFragment,
        bundleOf(ARTICLE_URL to url)
    )
}
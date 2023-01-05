package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_news_app.databinding.FragmentSearchBinding
import com.example.my_news_app.presentation.ClickCallBack
import com.example.my_news_app.presentation.viewModels.NewsViewModel
import com.example.my_news_app.presentation.viewModels.SearchViewModel
import com.example.my_news_app.presentation.adapter.ArticleAdapter
import com.example.my_news_app.utils.ResponseType
import com.example.my_news_app.utils.ViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNewsFragment:Fragment(),ClickCallBack {
    private var mBinding:FragmentSearchBinding? = null
    private val viewmodel: SearchViewModel by activityViewModels()
    private val newsViewModel: NewsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchBinding.inflate(inflater,container,false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding?.run {
            recyclerview.layoutManager = LinearLayoutManager(activity)
            viewmodel.result.observe(viewLifecycleOwner){ response->
                when (response) {
                    is ResponseType.Success -> {
                        response.data?.let {
                            val articles = it.map { ViewType.Article(it) }
                            recyclerview.adapter = ArticleAdapter(articles,this@SearchNewsFragment)
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


    override fun onDetach() {
        super.onDetach()
        newsViewModel.detach()

    }


    override fun onArticleClick(url: String) =  newsViewModel.articleClick(url)
}
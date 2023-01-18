package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
                            recyclerview.adapter =
                                ArticleAdapter(articles, onItemClick = ::handleItemClick)
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
        mBinding?.recyclerview?.adapter = null
        mBinding = null
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
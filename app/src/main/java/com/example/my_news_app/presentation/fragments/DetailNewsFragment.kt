package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.my_news_app.databinding.FragmentFullNewsBinding
import com.example.my_news_app.presentation.viewModels.MainViewModel
import com.example.my_news_app.constants.Constants.ARTICLE_URL

class DetailNewsFragment : Fragment() {
    private var mBinding: FragmentFullNewsBinding? = null
    private val viewmodel by activityViewModels<MainViewModel>()
    private var articleUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFullNewsBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.showProgressDialog()
        arguments?.let {
            articleUrl = it.getString(ARTICLE_URL)
        }
        mBinding?.let {
            with(it) {
                webview.webViewClient = WebClient()
                articleUrl?.let { url ->
                    webview.loadUrl(url)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    inner class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url!!)
            return false;
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewmodel.hideProgressDialog()
        }
    }

    companion object {
        fun String.DetailsNewsFragmentInstance() = DetailNewsFragment().apply {
            arguments = Bundle().apply { putString(ARTICLE_URL, this@DetailsNewsFragmentInstance) }
        }

    }
}
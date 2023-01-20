package com.example.my_news_app.presentation.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.my_news_app.R
import com.example.my_news_app.databinding.FragmentSavedArticleBinding
import dagger.hilt.android.AndroidEntryPoint

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

}
package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.my_news_app.databinding.FragmentSearchBinding

class SearchNewsFragment:Fragment() {
    private var mBinding:FragmentSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchBinding.inflate(inflater,container,false)
        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}
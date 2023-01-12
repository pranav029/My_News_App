package com.example.my_news_app.presentation.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.my_news_app.presentation.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseMainActivityFragment: Fragment(){
    val mainViewModel by activityViewModels<MainViewModel>()
}
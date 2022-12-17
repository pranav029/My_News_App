package com.example.my_news_app.utils

import androidx.fragment.app.FragmentFactory
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.MainActivity
import com.example.my_news_app.presentation.fragments.Display
import com.example.my_news_app.presentation.fragments.opt

class MyFragmentFactory(var con: MainActivity, var list:List<Article>, var status:Boolean): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): androidx.fragment.app.Fragment {
        return when(className){
            Display::class.java.name->Display(list,status,con)
            opt::class.java.name->{
                opt(con)
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}
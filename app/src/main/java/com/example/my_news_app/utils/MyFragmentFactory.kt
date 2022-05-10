package com.example.my_news_app.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.my_news_app.activities.MainActivity
import com.example.my_news_app.fragments.Display
import com.example.my_news_app.fragments.opt
import com.example.my_news_app.modal.Article

class MyFragmentFactory(var con:MainActivity,var list:ArrayList<Article>,var status:Boolean): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            Display::class.java.name->Display(list,status)
            opt::class.java.name->{
                opt(con)
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}
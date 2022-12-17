package com.example.my_news_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.R
import com.example.my_news_app.domain.model.Article
import com.example.my_news_app.presentation.ClickCallBack
import com.example.my_news_app.presentation.adapter.MyAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Display(val list: List<Article>, val progress: Boolean, val mListener: ClickCallBack) :
    Fragment() {
    lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (progress) {
            return inflater.inflate(R.layout.progress, container, false)
        }
//        val view = inflater.inflate(R.layout.sports_disp, container, false)
//        initRecyclerView(view)
        return inflater.inflate(R.layout.sports_disp, container, false)
            .also { initRecyclerView(it) }
    }

    private fun initRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = MyAdapter(list, mListener)
        recyclerView.adapter = adapter
    }
}
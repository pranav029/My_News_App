package com.example.my_news_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.R
import com.example.my_news_app.activities.MainActivity
import com.example.my_news_app.adaptor.MyAdaptor
import com.example.my_news_app.model.Article
import com.example.my_news_app.viewModel.NewsViewModel

class Display(val list:ArrayList<Article>,val progress:Boolean):Fragment() {
    lateinit var adapter: MyAdaptor
    lateinit var viewModel: NewsViewModel
    init{

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = (activity as MainActivity).viewModel
        if(progress==true){
            val view=inflater.inflate(R.layout.progress,container,false)
            return view
        }
        val view=inflater.inflate(R.layout.sports_disp,container,false)
        initRecyclerView(view)
        return view
    }

    fun initRecyclerView(view:View){
        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter=MyAdaptor(list,activity as MainActivity)
        recyclerView.adapter=adapter
    }
}
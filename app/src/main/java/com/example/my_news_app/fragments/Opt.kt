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
import com.example.my_news_app.adaptor.OptionAdapter

class opt(val con: MainActivity): Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.temp,container,false)
        val recyclerView=view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        var items=ArrayList<String>()
        with(items){
            add("News")
            add("Sports")
            add("Entertainment")
            add("Business")
            add("Technology")
            add("Health")
        }
        val adapter= OptionAdapter(items,con)
        recyclerView.adapter=adapter
        return view
    }
}
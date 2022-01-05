package com.example.my_news_app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.adaptor.MyAdaptor
import com.example.my_news_app.adaptor.OptionAdapter
import com.example.my_news_app.storage.Article

class opt(val con: Context,val call:forApi): Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.temp,container,false)
        val recyclerView=view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(con,LinearLayoutManager.HORIZONTAL,false)
        var items=ArrayList<String>()
        items.add("News")
        items.add("Sports")
        items.add("Entertainment")
        items.add("Business")
        items.add("Technology")
        items.add("Health")
        val adapter= OptionAdapter(items,con,call)
        recyclerView.adapter=adapter
        return view
    }
}
package com.example.my_news_app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.my_news_app.adaptor.MyAdaptor
import com.example.my_news_app.storage.Article
import org.json.JSONObject

@Suppress("RedundantSamConstructor")
class Display(val con:Context, val item:ArrayList<Article>,val progress:Boolean):Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(progress==true){
            val view=inflater.inflate(R.layout.progress,container,false)
            return view
        }
        val view=inflater.inflate(R.layout.sports_disp,container,false)
        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(con)
        val adapter=MyAdaptor(item,con)
        recyclerView.adapter=adapter
        return view
    }

}
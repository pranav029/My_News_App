package com.example.my_news_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.R
import com.example.my_news_app.activities.MainActivity

class OptionAdapter(private val mList: List<String>, private val con:MainActivity) : RecyclerView.Adapter<OptionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.option_disp, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val setv: String = mList[position]
        holder.textView.text= setv
        holder.itemView.setOnClickListener(){
            if(holder.textView.text=="Sports"){
                    con.getNews("sports")
            }
            if(holder.textView.text=="Business"){
                    con.getNews("business")
            }
            if(holder.textView.text=="Entertainment"){
                    con.getNews("entertainment")
            }
            if(holder.textView.text=="Technology"){
                    con.getNews("technology")
            }
            if(holder.textView.text=="Health"){
                    con.getNews("health")
            }
            if(holder.textView.text=="News"){
                    con.getNews("general")
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
   class ViewHolder(ItemView:View):RecyclerView.ViewHolder(ItemView){
        var textView:TextView=itemView.findViewById(R.id.optdisp)
    }


}
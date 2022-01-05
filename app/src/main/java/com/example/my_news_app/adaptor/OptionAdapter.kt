package com.example.my_news_app.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_news_app.R
import com.example.my_news_app.forApi

class OptionAdapter(private val mList: List<String>, private val con: Context,private val call:forApi) : RecyclerView.Adapter<OptionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.option_disp, parent, false)
        return OptionAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionAdapter.ViewHolder, position: Int) {
        val setv= mList[position]
        holder.textView.text= setv
        holder.itemView.setOnClickListener(){
            if(holder.textView.text=="Sports"){
                call.fetchdata("sports")
            }
            if(holder.textView.text=="Business"){
                call.fetchdata("business")
            }
            if(holder.textView.text=="Entertainment"){
                call.fetchdata("entertainment")
            }
            if(holder.textView.text=="Technology"){
                call.fetchdata("technology")
            }
            if(holder.textView.text=="Health"){
                call.fetchdata("health")
            }
            if(holder.textView.text=="News"){
                call.fetchdata("general")
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    public class ViewHolder(ItemView:View):RecyclerView.ViewHolder(ItemView){
        var textView:TextView=itemView.findViewById(R.id.optdisp)
    }


}